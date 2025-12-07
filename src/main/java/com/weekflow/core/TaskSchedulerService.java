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
     * STEP 1: 고정 일정 CSV 로드
     */
    public void loadFixedScheduleFromCSV(String path) {
        List<TimeBlock> blocks = FixedScheduleParser.parse(path);

        for (TimeBlock block : blocks) {
            schedule.addFixedTime(block);
        }

        System.out.println("✅ Fixed schedule loaded from: " + path);
    }

    /**
     * STEP 2: Task CSV 읽고 Task 자동 배정
     */
    public void loadTaskCSVAndSchedule(String path) {
        List<Task> tasks = TaskParser.parse(path);

        System.out.println("\n=== Scheduling Tasks from file: " + path + " ===");

        for (Task task : tasks) {
            autoScheduler.assignTask(task, schedule, freeTimeDetector);
        }
    }

    /**
     * STEP 3: 최종 스케줄을 CSV로 저장
     */
    public void exportFinalSchedule(String outputPath) {
        ScheduleCSVWriter.writeSchedule(schedule, outputPath);
        System.out.println("\n📤 Final schedule exported to: " + outputPath);
    }
}
