package controllers;

import model.Task;

import java.util.Objects;

class Node {
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