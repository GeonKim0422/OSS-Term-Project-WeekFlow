// src/com/weekflow/core/TaskAutoScheduler.java

package com.weekflow.core;

import java.time.LocalTime;
import java.util.List;
// import java.util.Random; // Removed, not needed for Best-Fit
// import java.util.stream.Collectors; // Removed, not needed for Best-Fit

public class TaskAutoScheduler {

    /**
     * Assigns a Task to the most suitable Free Time Block using the Best-Fit strategy.
     * The task list is pre-sorted by deadline/priority in TaskSchedulerService.
     * @param task The task to be assigned.
     * @param schedule The current FixedSchedule object to update.
     * @param detector The FreeTimeDetector to get current free blocks.
     * @return true if the task was successfully assigned, false otherwise.
     */
    public boolean assignTask(Task task, FixedSchedule schedule, FreeTimeDetector detector) {

        // 1. Calculate all available free blocks for the week
        List<TimeBlock> allFreeBlocks = detector.detectWeeklyFreeTime(schedule);

        // 2. Initialize variables for the Best-Fit search
        TimeBlock bestFitBlock = null;
        long minDurationDifference = Long.MAX_VALUE;
        long taskMinutes = task.getDurationMinutes();

        // 3. Iterate through all Free Blocks to find the Best-Fit
        for (TimeBlock freeBlock : allFreeBlocks) {
            long freeMinutes = freeBlock.getDurationMinutes();

            // Only consider blocks that can accommodate the task
            if (freeMinutes >= taskMinutes) {
                long durationDifference = freeMinutes - taskMinutes; // Remaining free time after placement

                // Best-Fit: Select the block that leaves the minimum remaining time
                if (durationDifference < minDurationDifference) {
                    minDurationDifference = durationDifference;
                    bestFitBlock = freeBlock;
                }
            }
        }

        // 4. Check if a suitable block was found
        if (bestFitBlock == null) {
            System.out.printf("❌ Task Assignment Failed: '%s' (%d min). No suitable free block found.\n", task.getTitle(), taskMinutes);
            return false;
        }

        // 5. Assign the task to the beginning of the best-fit block
        //    (This uses a First-Fit-in-Block strategy for simplicity and efficiency)
        LocalTime start = bestFitBlock.getStartTime();
        LocalTime end = start.plusMinutes(taskMinutes);

        // 6. Add the Task block to the schedule. FixedSchedule handles sorting by time.
        schedule.addFixedTime(new TimeBlock(
                bestFitBlock.getDay(),
                start,
                end,
                task.getTitle() // Use task title as the block name
        ));

        System.out.printf("✅ Task Assigned: '%s' -> %s %s~%s\n",
                task.getTitle(),
                bestFitBlock.getDay(),
                start,
                end);

        return true;
    }
}