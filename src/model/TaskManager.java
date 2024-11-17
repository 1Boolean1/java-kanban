package model;

import controllers.EpicTask;
import controllers.SubTask;
import controllers.Task;
import enums.Status;

import java.util.ArrayList;

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

}
