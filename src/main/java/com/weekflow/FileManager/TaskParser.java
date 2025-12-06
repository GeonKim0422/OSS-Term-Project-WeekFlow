package com.weekflow.FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.weekflow.core.Task;

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

                    String deadline = p[2].trim();
                    if (deadline.isEmpty()) deadline = null; // 빈칸이면 null 처리

                    int priority = 0;
                    if (p[3].trim().length() > 0) {
                        priority = Integer.parseInt(p[3].trim());
                    }

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
