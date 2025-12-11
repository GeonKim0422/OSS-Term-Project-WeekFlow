// src/com/weekflow/core/TaskSchedulerService.java

package com.weekflow.core;

import java.util.List;

public class TaskSchedulerService {

    private final FixedSchedule schedule;
    private final FreeTimeDetector freeTimeDetector;
    private final TaskAutoScheduler autoScheduler;

    public TaskSchedulerService() {
        this.schedule = new FixedSchedule();
        this.freeTimeDetector = new FreeTimeDetector();
        this.autoScheduler = new TaskAutoScheduler();
    }

    public FixedSchedule getSchedule() {
        return schedule;
    }

    /**
     * STEP 1: Load Fixed Schedule from CSV.
     */
    public void loadFixedScheduleFromCSV(String path) {
        List<TimeBlock> blocks = FixedScheduleParser.parse(path);

        for (TimeBlock block : blocks) {
            schedule.addFixedTime(block);
        }

        System.out.println("✅ Fixed schedule loaded from: " + path);
    }

    /**
     * STEP 2: Reads Task CSV, sorts tasks by deadline/priority, and assigns them.
     */
    public void loadTaskCSVAndSchedule(String path) {
        List<Task> tasks = TaskParser.parse(path);

        System.out.println("\n=== Scheduling Tasks from file: " + path + " ===");

        // Apply new sorting logic: Deadline-First, then Priority-Based
        tasks.sort(new Task.TaskComparator()); // Sort tasks before scheduling

        for (Task task : tasks) {
            autoScheduler.assignTask(task, schedule, freeTimeDetector);
        }
    }

    /**
     * STEP 3: Export Final Schedule to CSV.
     */
    public void exportFinalSchedule(String path) {
        ScheduleCSVWriter.writeSchedule(schedule, path);
    }
}