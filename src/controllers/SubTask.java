package controllers;

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