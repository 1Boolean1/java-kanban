package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final TreeSet<Task> tasksSortByTime = new TreeSet<>(Comparator.comparing(Task::getEndTime));
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
        if (!isTimeCrossing(task)) {
            final int id = ++numOfTasks;
            task.setTaskId(id);
            tasks.put(id, task);
            setNumOfTasks(id);
            if (task.getStartTime() != null) {
                tasksSortByTime.add(task);
            }
            return id;
        } else {
            return 0;
        }
    }

    @Override
    public int addNewEpicTask(EpicTask epicTask) {
        if (!isTimeCrossing(epicTask)) {
            final int id = ++numOfTasks;
            epicTask.setTaskId(id);
            epicTasks.put(id, epicTask);
            setNumOfTasks(id);
            return id;
        } else {
            return 0;
        }
    }

    @Override
    public Integer addNewSubTask(SubTask subTask) {
        if (!isTimeCrossing(subTask)) {
            EpicTask epicTask = epicTasks.get(subTask.getEpicId());
            if (epicTask == null) {
                return null;
            }
            final int id = ++numOfTasks;
            subTask.setTaskId(id);
            epicTask.addSubTask(subTask, id);
            epicTask.changeEpicTaskStatus();
            if (subTask.getStartTime() != null && subTask.getDuration() != null && !tasksSortByTime.contains(epicTask)) {
                tasksSortByTime.add(subTask);
            }
            return id;
        } else {
            return null;
        }
    }

    @Override
    public void updateTaskStatus(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).changeTaskStatus();
        } else {
            epicTasks.keySet().stream()
                    .filter(key -> epicTasks.get(key).getSubTasks().containsKey(taskId))
                    .forEach(key -> {
                        epicTasks.get(key).getSubTasks().get(taskId).changeTaskStatus();
                        epicTasks.get(key).changeEpicTaskStatus();
                    });
        }
    }

    @Override
    public ArrayList<Task> printAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(getTasks());
        tasks.addAll(getEpicTasks());
        epicTasks.values()
                .forEach(epicTask -> tasks.addAll(epicTask.getSubTasksArray()));
        return tasks;
    }

    @Override
    public boolean updateTask(Integer id, String newName, String newDescription) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setTaskName(newName);
            tasks.get(id).setTaskDescription(newDescription);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.get(id).setTaskName(newName);
            epicTasks.get(id).setTaskDescription(newDescription);
        } else {
            epicTasks.keySet().stream()
                    .filter(key -> epicTasks.get(key).getSubTasks().containsKey(id))
                    .forEach(key -> {
                        epicTasks.get(key).getSubTasks().get(id).setTaskName(newName);
                        epicTasks.get(key).getSubTasks().get(id).setTaskDescription(newDescription);
                        epicTasks.get(key).changeEpicTaskStatus();
                    });
        }
        return true;
    }

    @Override
    public boolean updateTask(Integer id, String newName, String newDescription, Duration duration, LocalDateTime time) {
        if (tasks.containsKey(id)) {
            if (!isTimeCrossing(tasks.get(id))) {
                tasks.get(id).setTaskName(newName);
                tasks.get(id).setTaskDescription(newDescription);
                tasks.get(id).setDuration(duration);
                tasks.get(id).setStartTime(time);
                return true;
            } else {
                return false;
            }
        } else if (epicTasks.containsKey(id)) {
            return false;
        } else {
            for (Integer key : epicTasks.keySet()) {
                if (epicTasks.get(key).getSubTasks().containsKey(id)) {
                    if (!isTimeCrossing(epicTasks.get(key))) {
                        epicTasks.get(key).getSubTasks().get(id).setTaskName(newName);
                        epicTasks.get(key).getSubTasks().get(id).setTaskDescription(newDescription);
                        epicTasks.get(key).getSubTasks().get(id).setDuration(duration);
                        epicTasks.get(key).getSubTasks().get(id).setStartTime(time);
                        epicTasks.get(key).changeEpicTaskStatus();
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public ArrayList<Task> printTasksByStatus(Status status) {
        ArrayList<Task> tasksToPrint = new ArrayList<>();
        tasks.keySet().stream()
                .filter(key -> tasks.get(key).getTaskStatus().equals(status))
                .forEach(key -> tasksToPrint.add(tasks.get(key)));
        epicTasks.keySet().stream()
                .filter(key -> epicTasks.get(key).getTaskStatus().equals(status))
                .forEach(key -> {
                    tasksToPrint.add(epicTasks.get(key));
                    epicTasks.get(key).getSubTasksArray().stream()
                            .filter(subTask -> subTask.getTaskStatus().equals(status))
                            .forEach(tasksToPrint::add);
                });
        return tasksToPrint;
    }

    @Override
    public void deleteById(Integer id) {
        if (tasks.containsKey(id)) {
            Task taskToRemove = tasks.get(id);
            tasks.remove(id);
            historyManager.remove(taskToRemove);
            tasksSortByTime.remove(taskToRemove);
        } else if (epicTasks.containsKey(id)) {
            EpicTask epicTaskToRemove = epicTasks.get(id);
            epicTasks.remove(id);
            historyManager.remove(epicTaskToRemove);
        } else {
            epicTasks.keySet().stream()
                    .filter(key -> epicTasks.get(key).getSubTasks().containsKey(id))
                    .forEach(key -> {
                        SubTask subTaskToRemove = epicTasks.get(key).getSubTasks().get(id);
                        epicTasks.get(key).getSubTasks().remove(id);
                        historyManager.remove(subTaskToRemove);
                        tasksSortByTime.remove(subTaskToRemove);
                        epicTasks.get(key).changeEpicTaskStatus();
                    });
        }
    }


    @Override
    public Task getTask(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.getOrDefault(taskId, null);
    }

    @Override
    public EpicTask getEpicTask(Integer taskId) {
        Managers.getDefaultHistory().add(epicTasks.get(taskId));
        return epicTasks.getOrDefault(taskId, null);
    }

    @Override
    public Task getSubTask(Integer taskId) {
        return epicTasks.values().stream()
                .map(epicTask -> epicTask.getSubTasks().get(taskId))
                .filter(Objects::nonNull)
                .peek(task -> Managers.getDefaultHistory().add(task))
                .findFirst()
                .orElse(null);
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
    public List<SubTask> getSubTasks() {
        return epicTasks.values().stream()
                .flatMap(epicTask -> epicTask.getSubTasksArray().stream()).collect(Collectors.toList());
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
        tasks.values().forEach(historyManager::remove);
        tasks.values().forEach(tasksSortByTime::remove);
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        epicTasks.values().forEach(epicTask -> {
                    epicTask.getSubTasks().values().forEach(historyManager::remove);
                    epicTask.getSubTasks().values().forEach(tasksSortByTime::remove);
                }
        );
        epicTasks.values().forEach(epicTask -> epicTask.getSubTasks().clear());
    }

    @Override
    public void deleteEpics() {
        epicTasks.values().forEach(historyManager::remove);
        epicTasks.clear();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return tasksSortByTime;
    }

    private boolean isTimeCrossing(Task task) {
        if (task.getStartTime() != null && !tasksSortByTime.isEmpty()) {
            return tasksSortByTime.stream()
                    .anyMatch(x ->
                            (x.getStartTime().isBefore(task.getEndTime()) && x.getEndTime().isAfter(task.getStartTime()))
                    );
        } else {
            return false;
        }
    }
}