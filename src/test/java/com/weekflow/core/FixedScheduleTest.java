package com.weekflow.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FixedSchedule 클래스에 대한 기본 단위 테스트를 수행합니다.
 * 이 테스트는 Issue #5 (Unit Test & CI Setup)의 기반을 마련합니다.
 * * NOTE: JUnit 5 (jupiter)를 사용합니다.
 */
class FixedScheduleTest {

    private FixedSchedule schedule;

    @BeforeEach
    void setUp() {
        // 각 테스트 시작 전에 새로운 FixedSchedule 객체를 생성하여 초기화합니다.
        schedule = new FixedSchedule();
    }

    @Test
    void testInitialEmptySchedule() {
        // 초기 상태: 모든 요일에 고정 일정이 없어야 합니다.
        for (DayOfWeek day : DayOfWeek.values()) {
            assertTrue(schedule.getFixedTimes(day).isEmpty(), day + " should be empty initially.");
        }
    }

    @Test
    void testAddFixedTimeBasic() {
        // 고정 일정 추가 테스트
        TimeBlock block = new TimeBlock(DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30));
        schedule.addFixedTime(block);

        List<TimeBlock> mondayTimes = schedule.getFixedTimes(DayOfWeek.MONDAY);
        assertEquals(1, mondayTimes.size(), "Monday should have one fixed block.");
        assertEquals(block, mondayTimes.get(0), "The added block should match.");
    }

    @Test
    void testFixedTimeSorting() {
        // 정렬 로직 테스트: 늦게 추가해도 시간 순서대로 정렬되어야 합니다.
        TimeBlock blockLate = new TimeBlock(DayOfWeek.TUESDAY,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0));
        TimeBlock blockEarly = new TimeBlock(DayOfWeek.TUESDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 30));

        // 늦은 시간을 먼저 추가
        schedule.addFixedTime(blockLate);
        // 이른 시간을 나중에 추가 (정렬되어야 함)
        schedule.addFixedTime(blockEarly);

        List<TimeBlock> tuesdayTimes = schedule.getFixedTimes(DayOfWeek.TUESDAY);
        assertEquals(2, tuesdayTimes.size(), "Tuesday should have two blocks.");

        // 정렬 검증: blockEarly가 첫 번째에 와야 합니다.
        assertEquals(blockEarly, tuesdayTimes.get(0), "Early block should be first.");
        assertEquals(blockLate, tuesdayTimes.get(1), "Late block should be second.");
    }

    @Test
    void testOverlapHandling_ShouldKeepSorted() {
        // 중복되는 시간을 추가해도 정렬은 유지되어야 합니다.
        // FixedSchedule은 중복 자체를 처리하지 않고 단순 추가 및 정렬만 하므로, 정렬에 집중합니다.
        TimeBlock block1 = new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        TimeBlock block2 = new TimeBlock(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)); // 겹침

        schedule.addFixedTime(block1);
        schedule.addFixedTime(block2);

        List<TimeBlock> wednesdayTimes = schedule.getFixedTimes(DayOfWeek.WEDNESDAY);

        // 정렬 검증: block2 (9:00 시작)가 block1 (10:00 시작)보다 앞에 와야 함
        assertEquals(block2, wednesdayTimes.get(0));
        assertEquals(block1, wednesdayTimes.get(1));
    }
}