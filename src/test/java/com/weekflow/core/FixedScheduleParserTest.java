package com.weekflow.core;

import com.weekflow.core.TimeBlock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

public class FixedScheduleParserTest {

    @Test
    public void testParse() throws Exception {

        String path = "test_fixed.csv";

        // 테스트용 CSV 파일 생성
        PrintWriter pw = new PrintWriter(new FileWriter(path));
        pw.println("type,start,end,day");
        pw.println("SLEEP,00:00,06:00,MON");
        pw.close();

        // 파싱 실행
        List<TimeBlock> blocks = FixedScheduleParser.parse(path);

        // 기본적인 검증
        assertEquals(1, blocks.size());

        TimeBlock b = blocks.get(0);
        assertEquals(DayOfWeek.MONDAY, b.getDay());
        assertEquals(LocalTime.of(0, 0), b.getStartTime());
        assertEquals(LocalTime.of(6, 0), b.getEndTime());
    }
}
