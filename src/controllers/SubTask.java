package controllers;

public class SubTask extends Task {

    public SubTask(String subTaskName, String subTaskDescription,
                   int subTaskId) {
        super(subTaskName, subTaskDescription, subTaskId);
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