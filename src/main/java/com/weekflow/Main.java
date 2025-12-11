package com.weekflow;

import com.weekflow.core.TaskSchedulerService;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        TaskSchedulerService service = new TaskSchedulerService();

        String basePath = "files/";

        // 1. 고정 스케줄 CSV 로드
        service.loadFixedScheduleFromCSV(basePath + "fixed_schedule.csv");

        // 2. 📌 files 폴더에서 "task"가 들어간 csv 자동 찾기
        File dir = new File(basePath);
        File[] taskFiles = dir.listFiles(f ->
                f.getName().toLowerCase().contains("task") &&
                f.getName().toLowerCase().endsWith(".csv")
        );

        if (taskFiles == null || taskFiles.length == 0) {
            System.out.println("⚠ Task CSV 파일을 찾을 수 없습니다.");
        } else {
            System.out.println("📂 발견된 task 파일 목록:");
            for (File tf : taskFiles) {
                System.out.println(" - " + tf.getName());
                service.loadTaskCSVAndSchedule(basePath + tf.getName());
            }
        }

        // 3. 최종 스케줄 저장
        service.exportFinalSchedule(basePath + "final_schedule.csv");

        System.out.println("🎉 Scheduling Completed!");
    }
}
