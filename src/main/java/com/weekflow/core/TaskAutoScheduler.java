package com.weekflow.core;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.DayOfWeek;


public class TaskAutoScheduler {

    private final Random random = new Random();

    public boolean assignTask(Task task, FixedSchedule schedule, FreeTimeDetector detector) {

        // 1) 전체 free blocks 가져오기
        List<TimeBlock> allFreeBlocks = detector.detectWeeklyFreeTime(schedule);

        // 2) free blocks를 요일별로 그룹화
        Map<DayOfWeek, List<TimeBlock>> byDay =
                allFreeBlocks.stream().collect(Collectors.groupingBy(TimeBlock::getDay));

        // 3) free time이 있는 요일들
        DayOfWeek[] days = byDay.keySet().toArray(DayOfWeek[]::new);

        if (days.length == 0) {
            System.out.println("⚠ No free days for task: " + task.getTitle());
            return false;
        }

        // ⭐ 4) 랜덤으로 요일 선택
        DayOfWeek chosenDay = days[random.nextInt(days.length)];
        List<TimeBlock> dailyFreeBlocks = byDay.get(chosenDay);

        if (dailyFreeBlocks == null || dailyFreeBlocks.isEmpty()) {
            System.out.println("⚠ No free blocks on selected day for task: " + task.getTitle());
            return false;
        }

        // ⭐ 5) 해당 요일 free block 중 랜덤 선택
        TimeBlock free = dailyFreeBlocks.get(random.nextInt(dailyFreeBlocks.size()));

        long freeMinutes = free.getDurationMinutes();
        long taskMinutes = task.getDurationMinutes();

        if (freeMinutes < taskMinutes) {
            System.out.println("⚠ Selected free block too small for task: " + task.getTitle());
            return false;
        }

        // ⭐ 6) free block 내부 랜덤 시간 선택
        long latestStart = freeMinutes - taskMinutes;
        long randomOffset = (latestStart > 0)
                ? random.nextInt((int) latestStart + 1)
                : 0;

        LocalTime start = free.getStartTime().plusMinutes(randomOffset);
        LocalTime end = start.plusMinutes(taskMinutes);

        // 7) 스케줄에 추가 (고정 블록으로)
        schedule.addFixedTime(new TimeBlock(
        free.getDay(),
        start,
        end,
        task.getTitle()     
        ));


        System.out.println("📌 Task scheduled: " + task.getTitle() +
                " (" + free.getDay() + " " + start + " ~ " + end + ")");

        return true;
    }
}
