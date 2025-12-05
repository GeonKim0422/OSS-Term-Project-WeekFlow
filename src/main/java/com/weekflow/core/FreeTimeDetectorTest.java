package com.weekflow.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * FreeTimeDetector 클래스에 대한 단위 테스트를 수행합니다.
 * Issue #2의 핵심 로직인 자유 시간 계산의 정확도를 검증합니다.
 */
class FreeTimeDetectorTest {

    private FreeTimeDetector detector;
    private FixedSchedule schedule;

    @BeforeEach
    void setUp() {
        detector = new FreeTimeDetector();
        schedule = new FixedSchedule();
    }

    // --- Daily Free Time Detection Tests (detectDailyFreeTime) ---

    @Test
    void testNoFixedSchedule_FullDayFree() {
        // Case 1: 고정 일정이 전혀 없을 때 (하루 전체가 자유 시간)
        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.MONDAY, Collections.emptyList());

        assertEquals(1, freeTimes.size(), "Should have exactly one full-day block.");
        TimeBlock fullDay = freeTimes.get(0);
        assertEquals(LocalTime.MIN, fullDay.getStart());
        // TimeBlockUtils.createFullDayBlock에서 LocalTime.MAX로 정의했으므로 이를 검증
        assertEquals(LocalTime.MAX, fullDay.getEnd());
        // 24시간을 분으로 변환: 24 * 60 = 1440분 (LocalTime.MAX는 1 나노초 미만이라 약간의 오차는 무시)
        assertTrue(fullDay.getDurationMinutes() >= 1439, "Full day should be close to 1440 minutes.");
    }

    @Test
    void testSingleFixedBlock_SplitsDayIntoTwo() {
        // Case 2: 고정 일정이 하나일 때 (자유 시간은 두 블록으로 분할됨)
        // 고정 일정: 10:00 ~ 12:00 (120분)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));

        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.TUESDAY, schedule.getFixedTimes(DayOfWeek.TUESDAY));

        assertEquals(2, freeTimes.size(), "Should be split into two free blocks.");

        // Block 1: 00:00 ~ 10:00
        assertEquals(LocalTime.MIN, freeTimes.get(0).getStart());
        assertEquals(LocalTime.of(10, 0), freeTimes.get(0).getEnd());

        // Block 2: 12:00 ~ 23:59:59.999...
        assertEquals(LocalTime.of(12, 0), freeTimes.get(1).getStart());
        assertEquals(LocalTime.MAX, freeTimes.get(1).getEnd());

        // 총 자유 시간 검증: 1440분 - 120분 = 1320분 (거의)
        long totalFreeTime = freeTimes.get(0).getDurationMinutes() + freeTimes.get(1).getDurationMinutes();
        assertTrue(totalFreeTime >= 1319, "Total free time should be close to 1320 minutes.");
    }

    @Test
    void testMultipleOverlappingBlocks_CorrectlyMerged() {
        // Case 3: 겹치는 고정 일정이 있을 때 (FreeTimeDetector에서 FixedSchedule은 정렬되므로, TimeBlockUtils에서 처리되어야 함)
        // FixedSchedule은 중복 처리를 하지 않고 정렬만 하므로, FreeTimeDetector는 모든 블록을 순서대로 빼야 합니다.

        // 고정 1: 08:00 ~ 10:00
        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)));
        // 고정 2: 09:30 ~ 11:30 (고정 1과 30분 겹침)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(11, 30)));

        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.WEDNESDAY, schedule.getFixedTimes(DayOfWeek.WEDNESDAY));

        // 최종 고정 시간대: 08:00 ~ 11:30 (210분)
        // 자유 시간: 00:00~08:00, 11:30~23:59:59...

        assertEquals(2, freeTimes.size(), "Should have two non-contiguous free blocks.");

        // Block 1: 00:00 ~ 08:00
        assertEquals(LocalTime.of(8, 0), freeTimes.get(0).getEnd());

        // Block 2: 11:30 ~ 23:59:59...
        assertEquals(LocalTime.of(11, 30), freeTimes.get(1).getStart());

        // 총 자유 시간 검증: 1440분 - 210분 = 1230분 (거의)
        long totalFreeTime = freeTimes.get(0).getDurationMinutes() + freeTimes.get(1).getDurationMinutes();
        assertTrue(totalFreeTime >= 1229, "Total free time should be close to 1230 minutes.");
    }

    // --- Weekly Free Time Detection Test (detectWeeklyFreeTime) ---

    @Test
    void testDetectWeeklyFreeTime_CorrectAggregation() {
        // 목요일 (목표: 120분 고정)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.THURSDAY, LocalTime.of(15, 0), LocalTime.of(17, 0))); // 120분 고정

        // 금요일 (목표: 60분 고정)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.FRIDAY, LocalTime.of(20, 0), LocalTime.of(21, 0))); // 60분 고정

        // 주의 모든 요일 데이터를 가져와서 주간 자유 시간 계산
        List<TimeBlock> weeklyFreeTime = detector.detectWeeklyFreeTime(schedule);

        // 일요일부터 토요일까지 총 7개의 Free Time Block 리스트가 반환되어야 합니다.
        // 고정 일정이 없는 5일(월, 화, 수, 토, 일) = 5 * 1 블록
        // 고정 일정이 있는 2일(목, 금) = 2 * 2 블록
        // 총 블록 수: 5 + 4 = 9개가 예상되지만, detector의 구현 방식에 따라 하루당 1개의 블록 리스트만 반환되어야 합니다.
        // (FreeTimeDetector는 주간 일정이 아닌, 모든 FreeTimeBlock을 하나의 리스트로 반환해야 함)

        // 총 블록 수 검증 (고정 일정 없는 날 1개 블록, 있는 날 2개 블록)
        assertEquals(1 * 5 + 2 * 2, weeklyFreeTime.size(), "Total free blocks should be 9 (5 full days + 4 split blocks).");

        // 총 자유 시간 검증
        // 7일 * 1440분 = 10080분 (총 시간)
        // 총 고정 시간: 목(120분) + 금(60분) = 180분
        // 총 자유 시간 예상: 10080분 - 180분 = 9900분

        long actualTotalFreeTime = weeklyFreeTime.stream()
                .mapToLong(TimeBlock::getDurationMinutes)
                .sum();

        assertTrue(actualTotalFreeTime >= 9899, "Total weekly free time should be close to 9900 minutes.");
    }
}