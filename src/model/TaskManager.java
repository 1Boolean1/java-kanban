package model;

import controllers.EpicTask;
import controllers.SubTask;
import controllers.Task;
import enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    private int numOfTasks = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    Scanner scanner = new Scanner(System.in);

    public void addTask() {
        String taskName = scanner.nextLine();
        String taskDescription = scanner.nextLine();
        Task task = new Task(taskName, taskDescription, getNumOfTasks() + 1);
        tasks.put(getNumOfTasks() + 1, task);
        setNumOfTasks(getNumOfTasks() + 1);
    }

    public void changeStatus(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.get(id).changeTaskStatus();
        } else if (epicTasks.containsKey(id)) {
            int subTaskId = scanner.nextInt();
            scanner.nextLine();
            epicTasks.get(id).changeSubTaskStatus(subTaskId);
        } else {
            System.out.println("error");
        }
    }

    public void updateTask(Integer id, String newName, String newDescription) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setTaskName(newName);
            tasks.get(id).setTaskDescription(newDescription);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.get(id).setTaskName(newName);
            epicTasks.get(id).setTaskDescription(newDescription);
        }
    }


    public void addSubTasks(Integer id) {
        if (epicTasks.containsKey(id)) {
            int numOfSubTasks = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < numOfSubTasks; i++) {
                String subTaskName = scanner.nextLine();
                String subTaskDescription = scanner.nextLine();
                SubTask subTask = new SubTask(subTaskName, subTaskDescription,
                        epicTasks.get(id).getSubTasks().size() + 1);
                epicTasks.get(id).addSubTask(subTask);
            }
        }
    }

    public void printTasksByStatus(Status status) {
        for (Integer key : tasks.keySet()) {
            if (tasks.get(key).getTaskStatus().equals(status)) {
                System.out.println(tasks.get(key));
            }
        }
        for (Integer key : epicTasks.keySet()) {
            for (int i = 0; i < epicTasks.get(key).getSubTasks().size(); i++) {
                if (epicTasks.get(key).getSubTasks().get(i).getTaskStatus().equals(status)) {
                    System.out.println(epicTasks.get(key).getSubTasks().get(i));
                }
            }
        }
    }

    public void deleteById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);
        } else {
            System.out.println("error");
        }
    }

    public void addEpicTask() {
        String epicTaskName = scanner.nextLine();
        String epicTaskDescription = scanner.nextLine();
        ArrayList<SubTask> subTasks = new ArrayList<>();
        int numOfSubTasks = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numOfSubTasks; i++) {
            String subTaskName = scanner.nextLine();
            String subTaskDescription = scanner.nextLine();
            SubTask subTask = new SubTask(subTaskName, subTaskDescription,
                    subTasks.size() + 1);
            subTasks.add(subTask);
        }
        EpicTask epicTask = new EpicTask(epicTaskName, epicTaskDescription,
                getNumOfTasks() + 1, subTasks);
        epicTasks.put(getNumOfTasks() + 1, epicTask);
        setNumOfTasks(getNumOfTasks() + 1);
    }

    public void printAllTasks() {
        System.out.println(tasks);
        System.out.println(epicTasks);
    }

    public void removeAllTasks() {
        tasks.clear();
        epicTasks.clear();
    }

    public void printTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
        } else if (epicTasks.containsKey(id)) {
            System.out.println(epicTasks.get(id));
        } else {
            System.out.println("error");
        }
    }

    public int getNumOfTasks() {
        return numOfTasks;
    }

    public void setNumOfTasks(int numOfTasks) {
        this.numOfTasks = numOfTasks;
    }
}
