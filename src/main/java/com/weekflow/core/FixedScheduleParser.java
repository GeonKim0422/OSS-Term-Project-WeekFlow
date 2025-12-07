package com.weekflow.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class FixedScheduleParser {

    public static List<TimeBlock> parse(String filePath) {
        List<TimeBlock> blocks = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                // 첫 줄 헤더 건너뛰기
                if (first) {
                    first = false;
                    continue;
                }

                String[] p = line.split(",");
                if (p.length < 4) continue;

                String startStr = p[1].trim();
                String endStr = p[2].trim();
                String dayStr = p[3].trim();

                LocalTime start = LocalTime.parse(startStr);
                LocalTime end = LocalTime.parse(endStr);

                // EVERYDAY → 7개의 TimeBlock 생성
                if (dayStr.equalsIgnoreCase("EVERYDAY")) {
                    for (DayOfWeek day : DayOfWeek.values()) {
                        blocks.add(new TimeBlock(day, start, end));
                    }
                } else {
                    DayOfWeek day = convertDay(dayStr);
                    if (day != null) {
                        blocks.add(new TimeBlock(day, start, end));
                    }
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("고정 일정 CSV 파싱 오류: " + e.getMessage());
        }

        return blocks;
    }

    // 문자열 → DayOfWeek
    private static DayOfWeek convertDay(String s) {
        s = s.toUpperCase();

        if (s.equals("MON")) return DayOfWeek.MONDAY;
        if (s.equals("TUE")) return DayOfWeek.TUESDAY;
        if (s.equals("WED")) return DayOfWeek.WEDNESDAY;
        if (s.equals("THU")) return DayOfWeek.THURSDAY;
        if (s.equals("FRI")) return DayOfWeek.FRIDAY;
        if (s.equals("SAT")) return DayOfWeek.SATURDAY;
        if (s.equals("SUN")) return DayOfWeek.SUNDAY;

        return null;
    }
}
