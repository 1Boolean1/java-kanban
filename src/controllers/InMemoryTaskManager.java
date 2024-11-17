package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int numOfTasks = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++numOfTasks;
        task.setTaskId(id);
        tasks.put(id, task);
        setNumOfTasks(id);
        return id;
    }

    @Override
    public int addNewEpicTask(EpicTask epicTask) {
        final int id = ++numOfTasks;
        epicTask.setTaskId(id);
        epicTasks.put(id, epicTask);
        setNumOfTasks(id);
        return id;
    }

    @Override
    public Integer addNewSubTask(SubTask subTask, int epicId) {
        EpicTask epicTask = epicTasks.get(epicId);
        if (epicTask == null) {
            return null;
        }
        final int id = ++numOfTasks;
        subTask.setEpicId(epicId);
        subTask.setTaskId(id);
        epicTask.addSubTask(subTask, id);
        epicTask.changeEpicTaskStatus();
        return id;
    }

    @Override
    public void updateTaskStatus(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).changeTaskStatus();
        } else {
            for (Integer key : epicTasks.keySet()) {
                if (epicTasks.get(key).getSubTasks().containsKey(taskId)) {
                    epicTasks.get(key).getSubTasks().get(taskId).changeTaskStatus();
                    epicTasks.get(key).changeEpicTaskStatus();
                }
            }
        }
    }

    @Override
    public ArrayList<Task> printAllTasks() {
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
        } else {
            for (Integer key : epicTasks.keySet()) {
                if (epicTasks.get(key).getSubTasks().containsKey(id)) {
                    epicTasks.get(key).getSubTasks().get(id).setTaskName(newName);
                    epicTasks.get(key).getSubTasks().get(id).setTaskDescription(newDescription);
                    epicTasks.get(key).changeEpicTaskStatus();
                }
            }
        }
    }


    @Override
    public ArrayList<Task> printTasksByStatus(Status status) {
        ArrayList<Task> tasksToPrint = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            if (tasks.get(key).getTaskStatus().equals(status)) {
                tasksToPrint.add(tasks.get(key));
            }
        }
        for (Integer key : epicTasks.keySet()) {
            if (epicTasks.get(key).getTaskStatus().equals(status)) {
                tasksToPrint.add(epicTasks.get(key));
                for (SubTask subTask : epicTasks.get(key).getSubTasksArray()) {
                    if (subTask.getTaskStatus().equals(status)) {
                        tasksToPrint.add(subTask);
                    }
                }
            }
        }
        return tasksToPrint;
    }

    @Override
    public void deleteById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);
        } else {
            for (Integer key : epicTasks.keySet()) {
                if (epicTasks.get(key).getSubTasks().containsKey(id)) {
                    epicTasks.get(key).getSubTasks().remove(id);
                    epicTasks.get(key).changeEpicTaskStatus();
                }
            }
        }
    }

    @Override
    public Task getTask(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.getOrDefault(taskId, null);
    }

    @Override
    public Task getEpicTask(Integer taskId) {
        Managers.getDefaultHistory().add(epicTasks.get(taskId));
        return epicTasks.getOrDefault(taskId, null);
    }

    @Override
    public Task getSubTask(Integer taskId) {
        for (Integer key : epicTasks.keySet()) {
            if (epicTasks.get(key).getSubTasks().containsKey(taskId)) {
                Managers.getDefaultHistory().add(epicTasks.get(key).getSubTasks().get(taskId));
                return epicTasks.get(key).getSubTasks().get(taskId);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasksById(Integer subTaskId) {
        return new ArrayList<>(epicTasks.get(subTaskId).getSubTasksArray());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            subTasks.addAll(epicTask.getSubTasksArray());
        }
        return subTasks;
    }

    @Override
    public ArrayList<Task> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public int getNumOfTasks() {
        return numOfTasks;
    }

    public void setNumOfTasks(int numOfTasks) {
        this.numOfTasks = numOfTasks;
    }


    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.getSubTasks().clear();
        }
    }

    @Override
    public void deleteEpics() {
        epicTasks.clear();
    }
}