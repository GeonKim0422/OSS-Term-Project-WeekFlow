package com.weekflow.core;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeBlock {

    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String taskName;   // ⭐ Task 이름 (null = 고정 스케줄)

    // 기존 fixed/ free block 생성자
    public TimeBlock(DayOfWeek day, LocalTime start, LocalTime end) {
        this(day, start, end, null);
    }

    // Task block 생성자
    public TimeBlock(DayOfWeek day, LocalTime start, LocalTime end, String taskName) {
        if (start.isAfter(end) || start.equals(end)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        this.day = day;
        this.startTime = start;
        this.endTime = end;
        this.taskName = taskName; // ⭐ taskName 저장
    }

    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    public String getTaskName() { return taskName; } // ⭐ Task인지 구분할 수 있음

    public boolean overlapsWith(TimeBlock other) {
        if (!this.day.equals(other.day)) return false;
        return this.startTime.isBefore(other.endTime) &&
               other.startTime.isBefore(this.endTime);
    }

    public long getDurationMinutes() {
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", 
            day, startTime, endTime,
            taskName == null ? "BLOCK" : taskName
        );
    }
}
