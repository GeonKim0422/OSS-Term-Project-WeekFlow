package com.weekflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager {
    private Map<Day, List<TimeSlot>> weeklySchedule;

    public ScheduleManager() {
        this.weeklySchedule = new HashMap<>();
        initializeSchedule();
    }

    private void initializeSchedule() {
        for (Day day : Day.values()) {
            weeklySchedule.put(day, new ArrayList<>());
        }
    }

    public void addFixedSchedule(Day day, TimeSlot slot) {
        weeklySchedule.get(day).add(slot);
    }

    public List<TimeSlot> getSchedule(Day day) {
        return weeklySchedule.get(day);
    }

    public Map<Day, List<TimeSlot>> getWeeklySchedule() {
        return weeklySchedule;
    }
}
