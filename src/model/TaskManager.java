package model;

import controllers.EpicTask;
import controllers.SubTask;
import controllers.Task;
import enums.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int numOfTasks = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();

    public int addNewTask(Task task) {
        final int id = ++numOfTasks;
        task.setTaskId(id);
        tasks.put(id, task);
        setNumOfTasks(id);
        return id;
    }

    public int addNewEpicTask(EpicTask epicTask) {
        final int id = ++numOfTasks;
        epicTask.setTaskId(id);
        epicTasks.put(id, epicTask);
        setNumOfTasks(id);
        return id;
    }

    public Integer addNewSubTask(SubTask subTask){
        final int epicId = subTask.getEpicId();
        EpicTask epicTask = epicTasks.get(epicId);
        if (epicTask == null) {
            return null;
        }
        final int id = ++numOfTasks;
        subTask.setTaskId(id);
        epicTask.addSubTask(subTask, id);
        epicTask.changeEpicTaskStatus();
        return id;
    }

    public void updateTaskStatus(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).changeTaskStatus();
        }
        for(Integer key : epicTasks.keySet()){
            if(epicTasks.get(key).getSubTasks().containsKey(taskId)){
                epicTasks.get(key).getSubTasks().get(taskId).changeTaskStatus();
                epicTasks.get(key).changeEpicTaskStatus();
            }
        }
    }

   /* public void updateSubTaskStatus(Integer epicTaskId, int subTaskId){
        if (epicTasks.containsKey(epicTaskId)) {
            epicTasks.get(epicTaskId).changeSubTaskStatus(subTaskId);
        }
    }*/

    public ArrayList<Task> printAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(getTasks());
        tasks.addAll(getEpicTasks());
        return tasks;
    }

    public void updateTask(Integer id, String newName, String newDescription) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setTaskName(newName);
            tasks.get(id).setTaskDescription(newDescription);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.get(id).setTaskName(newName);
            epicTasks.get(id).setTaskDescription(newDescription);
        }
        for(Integer key: epicTasks.keySet()){
            if(epicTasks.get(key).getSubTasks().containsKey(id)){
                epicTasks.get(key).getSubTasks().get(id).setTaskName(newName);
                epicTasks.get(key).getSubTasks().get(id).setTaskDescription(newDescription);
                epicTasks.get(key).changeEpicTaskStatus();
            }
        }
    }


    public ArrayList<Task> printTasksByStatus(Status status) {
        ArrayList<Task> tasksToPrint = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            if (tasks.get(key).getTaskStatus().equals(status)) {
                tasksToPrint.add(tasks.get(key));
            }
        }
        for (Integer key : epicTasks.keySet()) {
            if(epicTasks.get(key).getTaskStatus().equals(status)){
                tasksToPrint.add(epicTasks.get(key));
                for(SubTask subTask: epicTasks.get(key).getSubTasksArray()){
                    if(subTask.getTaskStatus().equals(status)){
                        tasksToPrint.add(subTask);
                    }
                }
            }
        }
        return tasksToPrint;
    }

    public void deleteById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);
        }
        for(Integer key: epicTasks.keySet()){
            if(epicTasks.get(key).getSubTasks().containsKey(id)){
                epicTasks.get(key).getSubTasks().remove(id);
                epicTasks.get(key).changeEpicTaskStatus();
            }
        }
    }


    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks(){
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for(EpicTask epicTask: epicTasks.values()){
            subTasks.addAll(epicTask.getSubTasksArray());
        }
        return subTasks;
    }

    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public int getNumOfTasks() {
        return numOfTasks;
    }

    public void setNumOfTasks(int numOfTasks) {
        this.numOfTasks = numOfTasks;
    }


    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        for(EpicTask epicTask: epicTasks.values()){
            epicTask.getSubTasks().clear();
        }
    }

    public void deleteEpics() {
        epicTasks.clear();
    }
}