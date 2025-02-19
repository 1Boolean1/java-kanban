package model;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String subTaskName, String subTaskDescription, int epicId) {
        super(subTaskName, subTaskDescription);
        this.epicId = epicId;
        setTaskStatus(Status.NEW);
    }

    public SubTask(String subTaskName, String subTaskDescription, Duration subTaskDuration, LocalDateTime subTaskStartTime) {
        super(subTaskName, subTaskDescription, subTaskDuration, subTaskStartTime);
    }

    public SubTask(int subTaskId, String subTaskName, String subTaskDescription, Status subTaskStatus, Duration subTaskDuration, LocalDateTime subTaskStartTime, int epicId) {
        super(subTaskId, subTaskName, subTaskDescription, subTaskStatus, subTaskDuration, subTaskStartTime);
        this.epicId = epicId;
    }

    public String getType() {
        return "SUB";
    }


    @Override
    public String toString() {
        if (getDuration() != null) {
            return "subTask{" + "subTaskName='" + getTaskName() + '\'' + ", subTaskDescription='" + getTaskDescription() + '\'' + ", subTaskId=" + getTaskId() + ", subTaskStatus=" + getTaskStatus() + ", subTaskDuration=" + getDuration().toMinutes() + ", subTaskStartTime=" + getStartTime() + '}';
        } else {
            return "subTask{" + "subTaskName='" + getTaskName() + '\'' + ", subTaskDescription='" + getTaskDescription() + '\'' + ", subTaskId=" + getTaskId() + ", subTaskStatus=" + getTaskStatus() + '}';
        }
    }
}