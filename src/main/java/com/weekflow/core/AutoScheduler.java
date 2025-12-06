package com.weekflow.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalTime;

public class AutoScheduler {

    /**
     * Task 리스트를 Free Time 블록들에 배치하여
     * 실제 일정(TimeBlock) 리스트로 변환해서 반환한다.
     */
    public List<TimeBlock> scheduleTasks(List<Task> tasks, List<TimeBlock> freeTimes) {

        List<TimeBlock> scheduled = new ArrayList<>();

        if (tasks == null || freeTimes == null) {
            return scheduled;
        }

        // 1. Task 우선순위 정렬
        tasks.sort(Comparator.comparing(Task::getPriority).reversed());

        // 2. Task 배치
        for (Task task : tasks) {

            TimeBlock best = findBestFit(freeTimes, task);

            if (best != null) {
                long duration = task.getDurationMinutes();

                // 배치 시간 계산
                LocalTime start = best.getStartTime();
                LocalTime end = start.plusMinutes(duration);

                TimeBlock taskBlock =
                        new TimeBlock(best.getDay(), start, end);
                scheduled.add(taskBlock);

                // FreeTime 분할
                if (end.isBefore(best.getEndTime())) {
                    TimeBlock remaining =
                            new TimeBlock(best.getDay(), end, best.getEndTime());

                    int idx = freeTimes.indexOf(best);
                    freeTimes.set(idx, remaining);
                } else {
                    freeTimes.remove(best);
                }

            } else {
                System.out.println("⚠️ Task '" + task.getTitle() + "' could not be scheduled.");
            }
        }

        return scheduled;
    }


    /**
     * 여러 FreeTime 중 Task Duration에 가장 잘 맞는 FreeTime을 선택하는 함수
     */
    private TimeBlock findBestFit(List<TimeBlock> freeTimes, Task task) {

        TimeBlock best = null;
        long bestWaste = Long.MAX_VALUE;

        long need = task.getDurationMinutes();

        for (TimeBlock free : freeTimes) {

            long freeDur = free.getDurationMinutes();

            if (freeDur >= need) {
                long waste = freeDur - need;
                if (waste < bestWaste) {
                    bestWaste = waste;
                    best = free;
                }
            }
        }

        return best;
    }
}




