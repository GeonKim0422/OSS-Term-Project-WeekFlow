package com.weekflow.core;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * FixedSchedule은 사용자의 주간 고정 일정을 관리하는 클래스입니다.
 * [이슈 #1: Fixed Schedule Management Structure 구현]
 */
public class FixedSchedule {

    // Map<요일, 해당 요일의 고정 일정 TimeBlock 리스트>
    private final Map<DayOfWeek, List<TimeBlock>> scheduleMap;

    public FixedSchedule() {
        // EnumMap은 DayOfWeek와 같은 Enum 타입을 키로 사용할 때 효율적입니다.
        this.scheduleMap = new EnumMap<>(DayOfWeek.class);

        // 초기화: 모든 요일별로 TimeBlock 리스트 초기화
        for (DayOfWeek day : DayOfWeek.values()) {
            scheduleMap.put(day, new ArrayList<>());
        }
    }

    /**
     * 특정 요일에 새로운 고정 일정 TimeBlock을 추가합니다.
     */
    public void addFixedTime(TimeBlock block) {
        List<TimeBlock> daySchedule = scheduleMap.get(block.getDay());

        // TODO: 팀원 A: 겹침 검사 로직 추가 (필요 시)

        daySchedule.add(block);

        // 정렬: 시간 순서대로 정렬 (남는 시간 계산 시 효율적)
        daySchedule.sort((b1, b2) -> b1.getStartTime().compareTo(b2.getStartTime()));
    }

    /**
     * 특정 요일의 모든 고정 일정을 가져옵니다.
     */
    public List<TimeBlock> getFixedTimes(DayOfWeek day) {
        return scheduleMap.get(day);
    }

    // --- 초기 데이터 설정을 위한 유틸리티 ---

    /**
     * 예시 고정 일정을 설정합니다. (테스트 및 초기 사용을 위해)
     */
    public void setupDefaultSchedule() {
        // 월요일 수업 (10:00 - 12:00)
        addFixedTime(new TimeBlock(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        // 화요일 수업 (14:00 - 16:00)
        addFixedTime(new TimeBlock(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)));
        // 매일 밤 12시부터 아침 7시까지 수면 시간 설정
        for (DayOfWeek day : DayOfWeek.values()) {
            addFixedTime(new TimeBlock(day, LocalTime.of(0, 0), LocalTime.of(7, 0)));
        }
    }
}