package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private final Map<Integer, Node> historyNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        int taskId = task.getTaskId();

        if (historyNodes.containsKey(taskId)) {
            removeNode(historyNodes.get(taskId));
        }

        Node newNode = new Node(task);
        linkLast(newNode);
        historyNodes.put(taskId, newNode);
    }

    @Override
    public void remove(int id) {
        Node node = historyNodes.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;

        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }

        return tasks;
    }

    private void linkLast(Node node) {
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        node.next = null;
        node.prev = null;
    }

    public class Node {
        public final Task task;
        public Node prev;
        public Node next;

        Node(Task task) {
            this.task = task;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Node node = (Node) object;
            return Objects.equals(task, node.task) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, prev, next);
        }
    }
}
