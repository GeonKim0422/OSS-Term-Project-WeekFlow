package com.weekflow.core;

import com.weekflow.core.FixedSchedule;
import com.weekflow.core.TimeBlock;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class ScheduleCSVWriterTest {

    @Test
    public void testWrite() throws Exception {

        FixedSchedule schedule = new FixedSchedule();

        // 테스트용 일정 추가
        schedule.addFixedTime(
            new TimeBlock(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(11,0))
        );

        String output = "output_test.csv";

        // CSV 생성 실행
        ScheduleCSVWriter.writeSchedule(schedule, output);

        BufferedReader br = new BufferedReader(new FileReader(output));

        String header = br.readLine();
        assertEquals("type,start,end,day", header);

        String line = br.readLine();
        assertNotNull(line);
        assertTrue(line.contains("MONDAY"));
        assertTrue(line.contains("09:00"));
        assertTrue(line.contains("11:00"));

        br.close();
    }
}
