package com.weekflow.FileManager;

import com.weekflow.core.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class TaskParserTest {

    @Test
    public void testSimpleTask() throws Exception {

        String path = "test_task.csv";

        // 테스트용 CSV 생성
        PrintWriter pw = new PrintWriter(new FileWriter(path));
        pw.println("title,duration");
        pw.println("Study,60");
        pw.close();

        List<Task> tasks = TaskParser.parse(path);

        assertEquals(1, tasks.size());

        Task t = tasks.get(0);
        assertEquals("Study", t.getTitle());
        assertEquals(60, t.getDurationMinutes());
    }

    @Test
    public void testFullTask() throws Exception {

        String path = "test_full_task.csv";

        PrintWriter pw = new PrintWriter(new FileWriter(path));
        pw.println("title,duration,deadline,priority");
        pw.println("Project,120,2025-01-10,2");
        pw.close();

        List<Task> tasks = TaskParser.parse(path);

        Task t = tasks.get(0);
        assertEquals("Project", t.getTitle());
        assertEquals(120, t.getDurationMinutes());
        assertEquals("2025-01-10", t.getDeadline());
        assertEquals(2, t.getPriority());
    }
}
