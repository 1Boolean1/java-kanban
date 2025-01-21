package model;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String taskName;
    private String taskDescription;
    private int taskId;
    private Status taskStatus;
    Duration duration;
    LocalDateTime startTime;


    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = Status.NEW;
    }

    public Task(String taskName, String taskDescription, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
        this.taskStatus = Status.NEW;
    }

    public Task(int taskId, String taskName, String taskDescription,
                Status taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public Task(int taskId, String taskName, String taskDescription,
                Status taskStatus, Duration duration, LocalDateTime startTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public String toString() {
        if (duration != null) {
            return "Task{" +
                    "taskName='" + taskName + '\'' +
                    ", taskDescription='" + taskDescription + '\'' +
                    ", taskId=" + taskId +
                    ", taskStatus=" + taskStatus +
                    ", taskDuration=" + duration.toMinutes() +
                    ", taskStartTime=" + startTime +
                    '}';
        } else {
            return "Task{" +
                    "taskName='" + taskName + '\'' +
                    ", taskDescription='" + taskDescription + '\'' +
                    ", taskId=" + taskId +
                    ", taskStatus=" + taskStatus +
                    '}';
        }
    }

    public String getType() {
        return "TASK";
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void changeTaskStatus() {
        if (getTaskStatus().equals(Status.NEW)) {
            setTaskStatus(Status.IN_PROGRESS);
        } else if (getTaskStatus().equals(Status.IN_PROGRESS)) {
            setTaskStatus(Status.DONE);
        }
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}