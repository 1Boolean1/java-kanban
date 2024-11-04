package controllers;

import enums.Status;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;

    public EpicTask(String epicTaskName, String epicTaskDescription, int epicTaskId, ArrayList<SubTask> subTasks) {
        super(epicTaskName, epicTaskDescription, epicTaskId);
        this.subTasks = subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        if(getTaskStatus().equals(Status.DONE)){
            setTaskStatus(Status.IN_PROGRESS);
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }


    @Override
    public String toString() {
        return "EpicTask{" +
                "EpicTaskName='" + getTaskName() + '\'' +
                ", EpicTaskDescription='" + getTaskDescription() + '\'' +
                ", EpicTaskID=" + getTaskId() +
                ", EpicTaskStatus=" + getTaskStatus() +
                ", subTasks=" + subTasks +
                '}';
    }

    public void changeSubTaskStatus(int subTaskId){
        subTaskId -= 1;
        if (getSubTasks().get(subTaskId).getTaskStatus().equals(Status.NEW)){
            getSubTasks().get(subTaskId).setTaskStatus(Status.IN_PROGRESS);
            System.out.println("subStatus changed to in progress");
            if(getTaskStatus().equals(Status.NEW)){
                setTaskStatus(Status.IN_PROGRESS);
                System.out.println("epicStatus changed to in progress");
            }
        } else if (getSubTasks().get(subTaskId).getTaskStatus().equals(Status.IN_PROGRESS)) {
            getSubTasks().get(subTaskId).setTaskStatus(Status.DONE);
            System.out.println("subStatus changed to done");
            int numOfSubTasksWithDone = 0;
            for(int i = 1; i <= getSubTasks().size(); i++){
                if(getSubTasks().
                        get(subTaskId).getTaskStatus().equals(Status.DONE)){
                    numOfSubTasksWithDone++;
                }
            }
            if (numOfSubTasksWithDone == getSubTasks().size()){
                setTaskStatus(Status.DONE);
                System.out.println("epicStatus changed to DONE");
            }
        } else {
            System.out.println("error");
        }
    }
}
