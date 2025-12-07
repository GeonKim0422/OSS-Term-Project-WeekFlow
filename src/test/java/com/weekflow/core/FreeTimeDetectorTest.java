package com.weekflow.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Performs comprehensive unit tests for the FreeTimeDetector class.
 * This ensures the accuracy of the core free time calculation logic (Issue #2).
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
        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.MONDAY, Collections.emptyList());

        assertEquals(1, freeTimes.size());
        TimeBlock fullDay = freeTimes.get(0);

        assertEquals(LocalTime.MIN, fullDay.getStartTime());
        assertEquals(LocalTime.MAX, fullDay.getEndTime());

        assertTrue(fullDay.getDurationMinutes() >= 1439);
    }

    @Test
    void testSingleFixedBlock_SplitsDayIntoTwo() {
        schedule.addFixedTime(new TimeBlock(DayOfWeek.TUESDAY,
                LocalTime.of(10, 0), LocalTime.of(12, 0)));

        List<TimeBlock> freeTimes =
                detector.detectDailyFreeTime(DayOfWeek.TUESDAY, schedule.getFixedTimes(DayOfWeek.TUESDAY));

        assertEquals(2, freeTimes.size());

        assertEquals(LocalTime.MIN, freeTimes.get(0).getStartTime());
        assertEquals(LocalTime.of(10, 0), freeTimes.get(0).getEndTime());

        assertEquals(LocalTime.of(12, 0), freeTimes.get(1).getStartTime());
        assertEquals(LocalTime.MAX, freeTimes.get(1).getEndTime());

        long totalFreeTime =
                freeTimes.get(0).getDurationMinutes() +
                freeTimes.get(1).getDurationMinutes();

        assertTrue(totalFreeTime >= 1319);
    }

    @Test
    void testMultipleOverlappingBlocks_CorrectlyMerged() {

        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY,
                LocalTime.of(8, 0), LocalTime.of(10, 0)));

        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 30), LocalTime.of(11, 30)));

        List<TimeBlock> freeTimes =
                detector.detectDailyFreeTime(DayOfWeek.WEDNESDAY, schedule.getFixedTimes(DayOfWeek.WEDNESDAY));

        assertEquals(2, freeTimes.size());

        assertEquals(LocalTime.MIN, freeTimes.get(0).getStartTime());
        assertEquals(LocalTime.of(8, 0), freeTimes.get(0).getEndTime());

        assertEquals(LocalTime.of(11, 30), freeTimes.get(1).getStartTime());
        assertEquals(LocalTime.MAX, freeTimes.get(1).getEndTime());

        long totalFreeTime =
                freeTimes.get(0).getDurationMinutes() +
                freeTimes.get(1).getDurationMinutes();

        assertTrue(totalFreeTime >= 1229);
    }

    // --- Weekly Free Time Detection Test ---

    @Test
    void testDetectWeeklyFreeTime_CorrectAggregation() {

        schedule.addFixedTime(new TimeBlock(DayOfWeek.THURSDAY,
                LocalTime.of(15, 0), LocalTime.of(17, 0)));

        schedule.addFixedTime(new TimeBlock(DayOfWeek.FRIDAY,
                LocalTime.of(20, 0), LocalTime.of(21, 0)));

        List<TimeBlock> weeklyFreeTime = detector.detectWeeklyFreeTime(schedule);

        assertEquals(9, weeklyFreeTime.size());

        long actualTotalFreeTime =
                weeklyFreeTime.stream()
                        .mapToLong(TimeBlock::getDurationMinutes)
                        .sum();

        assertTrue(actualTotalFreeTime >= 9899);
    }
}
