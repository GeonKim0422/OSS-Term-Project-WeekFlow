package com.weekflow.core;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.DayOfWeek;

public class ScheduleCSVWriter {

    public static void writeSchedule(FixedSchedule schedule, String outputPath) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {

            // CSV 헤더
            pw.println("type,start,end,day");

            // 요일 순서대로 출력
            for (DayOfWeek day : DayOfWeek.values()) {

                for (TimeBlock block : schedule.getFixedTimes(day)) {
                    pw.printf(
                        "%s,%s,%s,%s%n",
                        "BLOCK",                      // type
                        block.getStartTime(),        // start
                        block.getEndTime(),          // end
                        day.toString()               // MONDAY, TUESDAY...
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("CSV 저장 오류: " + e.getMessage());
        }
    }
}
