// src/com/weekflow/core/Task.java

package com.weekflow.core;

import java.time.LocalDate;
import java.util.Comparator; // Added import for Comparator

public class Task {

    private String title;              // Task title
    private int durationMinutes;       // Duration in minutes
    private LocalDate deadline;        // Deadline
    private int priority;              // Priority (0 is highest)

    // Default constructor
    public Task() {}

    // Main constructor
    public Task(String title, int durationMinutes, LocalDate deadline, int priority) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.deadline = deadline;
        this.priority = priority;
    }

    // Convenience constructor (omitting deadline, priority)
    public Task(String title, int durationMinutes) {
        this(title, durationMinutes, null, 0);
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * Comparator for sorting Tasks: Deadline-First (earliest) then Priority-Based (lower number).
     */
    public static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            // 1. Compare by Deadline (earliest first, non-null tasks prioritized)
            if (t1.getDeadline() != null && t2.getDeadline() != null) {
                int deadlineCompare = t1.getDeadline().compareTo(t2.getDeadline());
                if (deadlineCompare != 0) {
                    return deadlineCompare;
                }
            } else if (t1.getDeadline() != null) {
                // t1 has a deadline, t2 does not. t1 is prioritized.
                return -1;
            } else if (t2.getDeadline() != null) {
                // t2 has a deadline, t1 does not. t2 is prioritized.
                return 1;
            }
            // Deadlines are equal or both are null. Fall through to priority.

            // 2. Compare by Priority (lower number means higher priority)
            return Integer.compare(t1.getPriority(), t2.getPriority());
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
