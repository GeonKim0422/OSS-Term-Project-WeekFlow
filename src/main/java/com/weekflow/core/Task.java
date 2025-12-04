package com.weekflow.core;

public class Task {

    private String title;              // 작업 제목
    private int durationMinutes;       // 소요 시간 (분)
    private String deadline;           // 마감 기한 (문자열로 단순 처리)
    private int priority;              // 우선순위 (기본값 0)

    // 기본 생성자
    public Task() {}

    // 주 생성자
    public Task(String title, int durationMinutes, String deadline, int priority) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.deadline = deadline;
        this.priority = priority;
    }

    // 편의 생성자 (deadline, priority 생략)
    public Task(String title, int durationMinutes) {
        this(title, durationMinutes, null, 0);
    }

    // Getter 메서드들
    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", deadline='" + deadline + '\'' +
                ", priority=" + priority +
                '}';
    }
}

