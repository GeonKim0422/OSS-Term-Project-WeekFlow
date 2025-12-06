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
        // Case 1: Testing when no fixed schedule is present (the entire day should be free time)
        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.MONDAY, Collections.emptyList());

        assertEquals(1, freeTimes.size(), "Should have exactly one full-day block.");
        TimeBlock fullDay = freeTimes.get(0);
        assertEquals(LocalTime.MIN, fullDay.getStart());
        // Verifying against LocalTime.MAX. LocalTime.MAX represents the end of the day (23:59:59.999...).
        // The implementation must handle this boundary safely without attempting to call plusNanos(1).
        assertEquals(LocalTime.MAX, fullDay.getEnd());
        // Verification of total minutes: 24 hours * 60 = 1440 minutes (allowing slight variance due to LocalTime.MAX)
        assertTrue(fullDay.getDurationMinutes() >= 1439, "Full day should be close to 1440 minutes.");
    }

    @Test
    void testSingleFixedBlock_SplitsDayIntoTwo() {
        // Case 2: Testing a single fixed block (should split the free time into two blocks)
        // Fixed Block: 10:00 ~ 12:00 (120 minutes)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)));

        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.TUESDAY, schedule.getFixedTimes(DayOfWeek.TUESDAY));

        assertEquals(2, freeTimes.size(), "Should be split into two free blocks.");

        // Block 1: 00:00 ~ 10:00. Note: FreeTimeDetector must ensure the result is sorted by start time.
        assertEquals(LocalTime.MIN, freeTimes.get(0).getStart());
        assertEquals(LocalTime.of(10, 0), freeTimes.get(0).getEnd());

        // Block 2: 12:00 ~ 23:59:59.999...
        assertEquals(LocalTime.of(12, 0), freeTimes.get(1).getStart());
        assertEquals(LocalTime.MAX, freeTimes.get(1).getEnd());

        // Total free time verification: 1440 min - 120 min = 1320 min (approx.)
        long totalFreeTime = freeTimes.get(0).getDurationMinutes() + freeTimes.get(1).getDurationMinutes();
        assertTrue(totalFreeTime >= 1319, "Total free time should be close to 1320 minutes.");
    }

    @Test
    void testMultipleOverlappingBlocks_CorrectlyMerged() {
        // Case 3: Testing overlapping fixed blocks. FreeTimeDetector must process all blocks sequentially.

        // Fixed 1: 08:00 ~ 10:00
        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)));
        // Fixed 2: 09:30 ~ 11:30 (overlaps with Fixed 1 by 30 minutes)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(11, 30)));

        List<TimeBlock> freeTimes = detector.detectDailyFreeTime(DayOfWeek.WEDNESDAY, schedule.getFixedTimes(DayOfWeek.WEDNESDAY));

        // Final fixed time span: 08:00 ~ 11:30 (210 minutes)
        // Expected free time blocks (must be sorted): 00:00~08:00, 11:30~23:59:59...

        assertEquals(2, freeTimes.size(), "Should have two non-contiguous free blocks.");

        // Block 1: 00:00 ~ 08:00
        assertEquals(LocalTime.of(8, 0), freeTimes.get(0).getEnd());

        // Block 2: 11:30 ~ 23:59:59...
        assertEquals(LocalTime.of(11, 30), freeTimes.get(1).getStart());

        // Total free time verification: 1440 min - 210 min = 1230 min (approx.)
        long totalFreeTime = freeTimes.get(0).getDurationMinutes() + freeTimes.get(1).getDurationMinutes();
        assertTrue(totalFreeTime >= 1229, "Total free time should be close to 1230 minutes.");
    }

    // --- Weekly Free Time Detection Test (detectWeeklyFreeTime) ---

    @Test
    void testDetectWeeklyFreeTime_CorrectAggregation() {
        // Thursday (Target: 120 minutes fixed)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.THURSDAY, LocalTime.of(15, 0), LocalTime.of(17, 0))); // 120 minutes fixed

        // Friday (Target: 60 minutes fixed)
        schedule.addFixedTime(new TimeBlock(DayOfWeek.FRIDAY, LocalTime.of(20, 0), LocalTime.of(21, 0))); // 60 minutes fixed

        // Fetch all daily data and calculate weekly free time
        List<TimeBlock> weeklyFreeTime = detector.detectWeeklyFreeTime(schedule);

        // Expected total number of blocks (5 full days * 1 block) + (2 days * 2 split blocks) = 9
        assertEquals(1 * 5 + 2 * 2, weeklyFreeTime.size(), "Total free blocks should be 9 (5 full days + 4 split blocks).");

        // Total free time verification
        // Total time for 7 days * 1440 min = 10080 minutes
        // Total fixed time: Thu (120 min) + Fri (60 min) = 180 minutes
        // Expected total free time: 10080 min - 180 min = 9900 minutes (approx.)

        long actualTotalFreeTime = weeklyFreeTime.stream()
                .mapToLong(TimeBlock::getDurationMinutes)
                .sum();

        assertTrue(actualTotalFreeTime >= 9899, "Total weekly free time should be close to 9900 minutes.");
    }
}