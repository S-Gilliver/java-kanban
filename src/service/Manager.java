package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedId = 1;

    public void createTask(Task task) {
        task.setId(generatedId++);
        task.setStatus("NEW");
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(generatedId++);
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(generatedId++);
      //  subtask.setStatus("NEW");
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        ArrayList <Integer> subtaskId = new ArrayList<>();
        for (Integer i : subtasks.keySet()) {
            if (subtasks.get(i).getEpicId() == id) {
                subtaskId.add(i);
            }
        }
        for (Integer i = 0; i < subtaskId.size(); i++) {
            subtasks.remove(subtaskId.get(i));
        }
        epics.remove(id);
    }

    public void deleteSubtask(Integer id) {
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        for (Subtask subtaskInEpic : epic.getSubtasks()) {
            if (subtaskInEpic.getId() == id) {
                epic.getSubtasks().remove(subtaskInEpic);
            }
        }
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int epicId : epics.keySet()) {
            epics.get(epicId).getSubtasks().clear();
            updateEpicStatus(epics.get(epicId));
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey((epic.getId()))) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        ArrayList<Subtask> subtaskByEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Subtask subtaskId : epic.getSubtasks()) {
            subtaskByEpic.add(subtaskId);
        }
        return subtaskByEpic;
    }


    private void updateEpicStatus(Epic epic) {
        //(проверка на) изменение статуса епика после удаления или добавления подзадачи (subtask)
        int allSubtaskNEW = 0;
        int allSubtaskDONE = 0;
        int allSubtaskStatus = 0;
        String status = null;

        for (Subtask subtaskId : epic.getSubtasks()) {
            status = subtaskId.getStatus();
            allSubtaskStatus++;
            if (status.equals("NEW")) {
                allSubtaskNEW++;
            }
            if (status.equals("DONE")) {
                allSubtaskDONE++;
            }
        }

        if (allSubtaskNEW == allSubtaskStatus) {
            epic.setStatus("NEW");
        } else if (allSubtaskDONE == allSubtaskStatus) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task getEpicById(int id) {
        return epics.get(id);
    }

    public Task getSubtaskById(int id) {
        return subtasks.get(id);
    }
}
