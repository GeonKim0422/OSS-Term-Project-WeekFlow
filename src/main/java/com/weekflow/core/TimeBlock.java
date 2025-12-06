package com.weekflow.core;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;

/**
 * TimeBlock 클래스는 요일(DayOfWeek)과 시작/종료 시간(LocalTime)을 포함하여
 * 고정 일정(Fixed Schedule) 또는 남는 시간대(Free Time)를 나타냅니다.
 * 이 클래스는 모든 일정 계산의 기본 단위입니다.
 */
public class TimeBlock {

    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private String title; // 추가


    public TimeBlock(DayOfWeek day, LocalTime start, LocalTime end, String title) {
        if (start.isAfter(end) || start.equals(end)) {
            // 시간 유효성 검사
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
        this.day = day;
        this.startTime = start;
        this.endTime = end;
        this.title = title;

    }

    // --- Getter Methods ---

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // --- Core Logic Methods (팀원 A가 구현할 핵심 로직) ---

    /**
     * 두 TimeBlock이 시간이 겹치는지 확인합니다.
     * 요일이 같고, 시간대가 서로 침범하는지 검사합니다.
     * @param other 비교할 다른 TimeBlock
     * @return 겹치면 true, 아니면 false
     */
    public boolean overlapsWith(TimeBlock other) {
        if (!this.day.equals(other.day)) {
            return false;
        }
        // A.Start < B.End && B.Start < A.End
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    /**
     * 현재 블록의 길이를 분 단위로 계산합니다.
     * @return 블록의 총 길이 (분 단위)
     */
    public long getDurationMinutes() {
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    // --- Utility ---

    @Override
    public String toString() {
        return String.format("[%s] %s - %s | %s (total %dmin)", day, startTime, endTime, title, getDurationMinutes());
    }
}