package com.weekflow.core;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TimeBlock 관련 유틸리티 함수를 모아둔 클래스입니다.
 * [이슈 #2: Free Time Detection Logic의 기반]
 */
public class TimeBlockUtils {

    /**
     * 하루 전체 TimeBlock을 생성합니다. (00:00부터 24:00 직전까지)
     */
    public static TimeBlock createFullDayBlock(DayOfWeek day) {
        return new TimeBlock(day, LocalTime.MIDNIGHT, LocalTime.MAX.plusNanos(1));
    }

    /**
     * 하나의 Free Time 블록에서 특정 Fixed Time 블록을 제외하여
     * 새로운 Free Time 블록 리스트를 생성합니다. (남는 시간 계산의 핵심 로직)
     * @param freeBlock 현재 남는 시간 블록
     * @param fixedBlock 제외할 고정 일정 블록
     * @return 제외 후 남은 TimeBlock 리스트 (0개, 1개 또는 2개)
     */
    public static List<TimeBlock> subtractFixedTime(TimeBlock freeBlock, TimeBlock fixedBlock) {
        List<TimeBlock> result = new ArrayList<>();

        // 1. 겹치지 않으면 원본 Free Block 반환
        if (!freeBlock.overlapsWith(fixedBlock)) {
            result.add(freeBlock);
            return result;
        }

        // 2. 겹치는 경우, 남은 시간 분할 로직 수행

        // A. Fixed Time의 시작이 Free Time의 시작보다 늦은 경우 (앞부분이 남음)
        if (fixedBlock.getStartTime().isAfter(freeBlock.getStartTime())) {
            // 예시: Free(9-17), Fixed(11-14) -> 앞부분 (9-11) 남음
            result.add(new TimeBlock(freeBlock.getDay(), freeBlock.getStartTime(), fixedBlock.getStartTime()));
        }

        // B. Fixed Time의 끝이 Free Time의 끝보다 이른 경우 (뒷부분이 남음)
        if (fixedBlock.getEndTime().isBefore(freeBlock.getEndTime())) {
            // 예시: Free(9-17), Fixed(11-14) -> 뒷부분 (14-17) 남음
            result.add(new TimeBlock(freeBlock.getDay(), fixedBlock.getEndTime(), freeBlock.getEndTime()));
        }

        return result;
    }
}