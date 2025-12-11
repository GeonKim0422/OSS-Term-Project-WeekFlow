package com.weekflow.core;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.DayOfWeek;

public class ScheduleCSVWriter {

    public static void writeSchedule(FixedSchedule schedule, String outputPath) {

        // 🔥 1. 파일 이름 충돌 처리 (final_schedule.csv → final_schedule_1.csv → _2 ...)
        File file = new File(outputPath);
        if (file.exists()) {
            int version = 1;
            String baseName = outputPath.replace(".csv", "");

            while (file.exists()) {
                file = new File(baseName + "_" + version + ".csv");
                version++;
            }

            outputPath = file.getAbsolutePath();
            System.out.println("⚠ 기존 파일이 열려있어 새로운 이름으로 저장합니다: " + outputPath);
        }

        // 🔥 2. CSV 저장
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {

            pw.println("type,start,end,day");

            for (DayOfWeek day : DayOfWeek.values()) {

                for (TimeBlock block : schedule.getFixedTimes(day)) {

                    String type = (block.getTaskName() == null)
                            ? "BLOCK"
                            : block.getTaskName();

                    pw.printf("%s,%s,%s,%s%n",
                            type,
                            block.getStartTime(),
                            block.getEndTime(),
                            day.toString());
                }
            }

            System.out.println("📤 Final schedule exported to: " + outputPath);

        } catch (Exception e) {
            System.out.println("CSV 저장 오류: " + e.getMessage());
        }
    }
}
