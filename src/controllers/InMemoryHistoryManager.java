package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        tasksHistory.add(task);
        if (tasksHistory.size() > 10) {
            tasksHistory.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(tasksHistory);
    }
}
