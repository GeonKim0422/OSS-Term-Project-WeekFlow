package com.weekflow;

import com.weekflow.core.TimeBlock;


/**
 * WeekFlow 프로젝트의 메인 실행 클래스입니다.
 * CLI 기반 인터페이스의 시작점 역할을 합니다.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("WeekFlow - 남는 시간 기반 역스케줄링 플래너가 시작됩니다.");

        // --- MVP 기능 실행 예시 ---
        // === AutoScheduler 테스트 ===
        System.out.println("=== WeekFlow Auto Scheduler Started ===");

        // --- MVP Example ---


        // 1. TimeBlock 클래스 테스트 (팀원 A의 작업)
        // TimeBlock morningFreeTime = new TimeBlock(DayOfWeek.MONDAY, LocalTime.of(7, 0), LocalTime.of(9, 0));
        // System.out.println("남는 시간 블록: " + morningFreeTime);

        // 2. CLI 인터페이스 로직 실행 (팀원 C의 작업)
        // new CliInterface().start();


        System.out.println("프로그램을 종료합니다.");


    }
}