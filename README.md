# OSS-Term-Project-WeekFlow
Open Source Planner for Slack-Based Backward Scheduling

This project is part of a university course. External contributions are not accepted until December 2025.

---

## Installation

### Requirements
- JDK 17
- Git
- Gradle (wrapper included)

### Clone the Repository
git clone https://github.com/GeonKim0422/OSS-Term-Project-WeekFlow.git  
cd OSS-Term-Project-WeekFlow

### Build the Project
./gradlew build

---

## Usage

The project runs as a CLI-based application. After building the project, use the following commands to execute it.

### 1. Run with Gradle
./gradlew run

### 2. Run the JAR file
java -jar build/libs/weekflow.jar --schedule \<fixed_schedule.csv\>

### 3. Command-line Options
--schedule \<file\>   Path to the fixed schedule CSV file  
--tasks \<file\>      Path to the task CSV file (optional)  
--output \<file\>     Path to the output file (optional)

## Usage Example

### When a task is successfully assigned
 Task Assigned: 'Study' → MON 13:00~15:00



