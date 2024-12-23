package model;

import enums.Status;

public class SubTask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String subTaskName, String subTaskDescription) {
        super(subTaskName, subTaskDescription);
    }

    public SubTask(int subTaskId, String subTaskName, String subTaskDescription, Status subTaskStatus, int epicId) {
        super(subTaskId, subTaskName, subTaskDescription, subTaskStatus);
        this.epicId = epicId;
    }

    public String getType() {
        return "SUB";
    }


    @Override
    public String toString() {
        return "subTask{" +
                "subTaskName='" + getTaskName() + '\'' +
                ", subTaskDescription='" + getTaskDescription() + '\'' +
                ", subTaskId=" + getTaskId() +
                ", subTaskStatus=" + getTaskStatus() +
                '}';
    }
}