package model;

import controllers.Task;

import java.util.ArrayList;

public interface HistoryManager {
    ArrayList<Task> tasksHistory = new ArrayList<>();

    void add(Task task);

    ArrayList<Task> getHistory();
}
