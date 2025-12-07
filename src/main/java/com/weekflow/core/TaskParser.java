package com.weekflow.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskParser {

    public static List<Task> parse(String filePath) {
        List<Task> tasks = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {

                // 헤더 스킵
                if (first) {
                    first = false;
                    continue;
                }

                String[] p = line.split(",");

                // title, durationMinutes 만 있는 형태
                if (p.length == 2) {
                    String title = p[0].trim();
                    int duration = Integer.parseInt(p[1].trim());

                    tasks.add(new Task(title, duration));
                }

                // title, duration, deadline, priority 4개 모두 있는 경우
                else if (p.length >= 4) {

                    String title = p[0].trim();
                    int duration = Integer.parseInt(p[1].trim());

                    // deadline — String → LocalDate 변환
                    LocalDate deadline = null;
                    if (!p[2].trim().isEmpty()) {
                        deadline = LocalDate.parse(p[2].trim());
                    }

                    int priority = 0;
                    if (!p[3].trim().isEmpty()) {
                        priority = Integer.parseInt(p[3].trim());
                    }

                    // 🔥 Task의 실제 생성자(LocalDate)를 사용해야 함
                    tasks.add(new Task(title, duration, deadline, priority));
                }
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Task CSV 파싱 오류: " + e.getMessage());
        }

        return tasks;
    }
}
