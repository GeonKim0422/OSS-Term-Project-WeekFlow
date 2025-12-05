package com.weekflow.core;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FreeTimeDetector 클래스는 FixedSchedule을 기반으로
 * 사용자의 주간 남는 시간(Free Time)을 계산하는 핵심 로직을 담당합니다.
 * 팀원 B가 이 클래스를 완성합니다. [이슈 #2 해결]
 */
public class FreeTimeDetector {

    /**
     * 특정 FixedSchedule에 대해 주간 전체의 Free Time Block 리스트를 반환합니다.
     * @param schedule 사용자의 고정 일정을 담고 있는 FixedSchedule 객체
     * @return 일주일 전체의 Free TimeBlock 리스트
     */
    public List<TimeBlock> detectWeeklyFreeTime(FixedSchedule schedule) {
        List<TimeBlock> weeklyFreeTime = new ArrayList<>();

        // 일주일 전체를 순회하며 남는 시간을 계산합니다.
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklyFreeTime.addAll(detectDailyFreeTime(day, schedule.getFixedTimes(day)));
        }

        return weeklyFreeTime;
    }

    /**
     * 특정 요일의 고정 일정을 제외하여 해당 요일의 Free Time Block 리스트를 계산합니다.
     * 이것이 Issue #2의 가장 핵심적인 구현 부분입니다.
     * * @param day 계산할 요일
     * @param fixedBlocks 해당 요일의 정렬된 고정 일정 리스트
     * @return 해당 요일의 Free TimeBlock 리스트
     */
    public List<TimeBlock> detectDailyFreeTime(DayOfWeek day, List<TimeBlock> fixedBlocks) {

        // 1. 초기 상태: 하루 전체를 남는 시간 블록으로 가정합니다.
        List<TimeBlock> freeTime = new ArrayList<>();
        freeTime.add(TimeBlockUtils.createFullDayBlock(day));

        // 2. 고정 일정을 하나씩 순회하며 남는 시간에서 제외합니다.
        for (TimeBlock fixed : fixedBlocks) {

            List<TimeBlock> updatedFreeTime = new ArrayList<>();

            // 현재까지 계산된 모든 '남는 시간' 블록에 대해 Fixed Time을 뺍니다.
            for (TimeBlock currentFree : freeTime) {
                // TimeBlockUtils.subtractFixedTime이 핵심적인 차집합 연산을 수행합니다.
                updatedFreeTime.addAll(TimeBlockUtils.subtractFixedTime(currentFree, fixed));
            }

            // Free Time 리스트를 갱신합니다.
            freeTime = updatedFreeTime;
        }

        // 3. (선택적) 시간이 0인 블록은 제외하고 정렬합니다.
        freeTime.removeIf(block -> block.getDurationMinutes() <= 0);

        return freeTime;
    }
}