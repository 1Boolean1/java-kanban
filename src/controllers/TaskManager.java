package controllers;

import enums.Status;
import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int addNewTask(Task task);

    int addNewEpicTask(EpicTask epicTask);

    Integer addNewSubTask(SubTask subTask, int id);

    ArrayList<Task> printAllTasks();

    ArrayList<Task> printTasksByStatus(Status status);

    void deleteById(Integer id);

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpics();

    void updateTaskStatus(Integer taskId);

    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<SubTask> getSubTasksById(Integer subTaskId);

    ArrayList<Task> getEpicTasks();

    Task getTask(Integer taskId);

    Task getEpicTask(Integer taskId);

    Task getSubTask(Integer taskId);

    List<Task> getHistory();

}
