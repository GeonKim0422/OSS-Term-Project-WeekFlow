package com.weekflow;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ScheduleManager scheduleManager = new ScheduleManager();
        inputFixedSchedule(scheduleManager);
        printSchedule(scheduleManager);
    }

    private static void inputFixedSchedule(ScheduleManager scheduleManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your fixed schedule (e.g., Sleep, Class).");
        System.out.println("Type 'exit' as the day to finish.");

        while (true) {
            try {
                System.out.print("\nEnter Day (MONDAY, TUESDAY, etc.): ");
                String dayInput = scanner.nextLine().trim().toUpperCase();

                if (dayInput.equals("EXIT")) {
                    break;
                }

                Day day = Day.valueOf(dayInput);

                System.out.print("Enter Start Time (HH:mm): ");
                LocalTime startTime = LocalTime.parse(scanner.nextLine().trim());

                System.out.print("Enter End Time (HH:mm): ");
                LocalTime endTime = LocalTime.parse(scanner.nextLine().trim());

                System.out.print("Enter Description: ");
                String description = scanner.nextLine().trim();

                TimeSlot slot = new TimeSlot(startTime, endTime, description);
                scheduleManager.addFixedSchedule(day, slot);
                System.out.println("Schedule added!");

            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Invalid input. Please try again. Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printSchedule(ScheduleManager scheduleManager) {
        System.out.println("\n--- Weekly Fixed Schedule ---");
        for (Day day : Day.values()) {
            System.out.println(day + ":");
            for (TimeSlot slot : scheduleManager.getSchedule(day)) {
                System.out.println("  " + slot);
            }
        }
    }
}
