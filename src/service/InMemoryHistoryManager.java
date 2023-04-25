package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodes = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public void add(Task task) {
        linkLast(task);
        nodes.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.remove(id));
        nodes.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private List<Task> getTask() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task, tail, null);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private void removeNode(Node node) {
        if (head == null || node == null) {
            return;
        }
        final Node next = node.next;
        final Node prev = node.prev;
        if (head == node) {
            head = next;
            head.prev = null;
        } else if (tail == node) {
            tail = prev;
            tail.next = null;
        }
        if (next != null) {
            next.prev = prev;
        }
        if (prev != null) {
            prev.next = next;
        }
    }
    private class Node {
        public Task task;
        public Node prev;
        public Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}