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

                if (first) {
                    first = false;
                    continue;
                }

                String[] p = line.split(",");
                if (p.length < 4) continue;

                String name = p[0].trim();   // ⭐ type -> TimeBlock 이름
                String startStr = p[1].trim();
                String endStr = p[2].trim();
                String dayStr = p[3].trim();

                LocalTime start = LocalTime.parse(startStr);
                LocalTime end = LocalTime.parse(endStr);

                if (dayStr.equalsIgnoreCase("EVERYDAY")) {
                    for (DayOfWeek day : DayOfWeek.values()) {
                        blocks.add(new TimeBlock(day, start, end, name)); // ⭐ 이름 전달
                    }
                } else {
                    DayOfWeek day = convertDay(dayStr);
                    if (day != null) {
                        blocks.add(new TimeBlock(day, start, end, name)); // ⭐ 이름 전달
                    }
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("고정 일정 CSV 파싱 오류: " + e.getMessage());
        }

        return blocks;
    }

    private static DayOfWeek convertDay(String s) {
    s = s.toUpperCase();

    if (s.equals("MON") || s.equals("MONDAY")) return DayOfWeek.MONDAY;
    if (s.equals("TUE") || s.equals("TUESDAY")) return DayOfWeek.TUESDAY;
    if (s.equals("WED") || s.equals("WEDNESDAY")) return DayOfWeek.WEDNESDAY;
    if (s.equals("THU") || s.equals("THURSDAY")) return DayOfWeek.THURSDAY;
    if (s.equals("FRI") || s.equals("FRIDAY")) return DayOfWeek.FRIDAY;
    if (s.equals("SAT") || s.equals("SATURDAY")) return DayOfWeek.SATURDAY;
    if (s.equals("SUN") || s.equals("SUNDAY")) return DayOfWeek.SUNDAY;

    return null;
}

}
