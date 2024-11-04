package controllers;

import enums.Status;

public class Task {
    private String taskName;
    private String taskDescription;
    private final int taskId;
    private Status taskStatus;


    public Task(String taskName, String taskDescription, int taskId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = Status.NEW;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", taskStatus=" + taskStatus +
                '}';
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
            System.out.println("taskStatus changed to in progress");
        } else if (getTaskStatus().equals(Status.IN_PROGRESS)) {
            setTaskStatus(Status.DONE);
            System.out.println("taskStatus changed to done");
        } else {
            System.out.println("error");
        }
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}