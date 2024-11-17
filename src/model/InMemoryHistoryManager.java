package model;

import controllers.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    @Override
    public void add(Task task) {
        if (tasksHistory.size() < 10) {
            tasksHistory.add(task);
        } else {
            tasksHistory.removeFirst();
            tasksHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return tasksHistory;
    }
}
