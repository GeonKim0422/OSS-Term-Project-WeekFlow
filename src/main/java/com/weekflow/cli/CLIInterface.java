package com.weekflow.cli;


import java.util.Scanner;

public class CLIInterface {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("=== WeekFlow CLI Interface ===");

        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> enterFixedSchedule();
                case 2 -> enterTasks();
                case 3 -> checkFreeTime();
                case 4 -> autoScheduleTasks();
                case 5 -> showSchedulerUI();
                case 0 -> {
                    System.out.println("Exiting WeekFlow. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Enter Fixed Weekly Schedule");
        System.out.println("2. Enter Tasks");
        System.out.println("3. Check Free Time");
        System.out.println("4. Auto Schedule Tasks");
        System.out.println("5. Show Scheduler UI");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1; // invalid input
        }
    }

    private void enterFixedSchedule() {
        System.out.println("[Enter Fixed Weekly Schedule]");
        // TODO: 연결 - FixedScheduleManager 로직
    }

    private void enterTasks() {
        System.out.println("[Enter Tasks]");
        // TODO: 연결 - TaskManager 로직
    }

    private void checkFreeTime() {
        System.out.println("[Check Free Time]");
        // TODO: 연결 - FreeTimeDetector 로직
    }

    private void autoScheduleTasks() {
        System.out.println("[Auto Schedule Tasks]");
        // TODO: 연결 - AutoScheduler 로직
    }

    private void showSchedulerUI() {
        System.out.println("[Show Scheduler UI]");
        // TODO: 간단한 ASCII UI 출력 또는 팀원 UI 모듈 연결
    }
}
