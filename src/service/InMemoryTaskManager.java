package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();
    private int generatedId = 1;


    @Override
    public void createTask(Task task) {
        task.setId(generatedId++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generatedId++);
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generatedId++);
        subtask.setStatus(Status.NEW);
        final Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Integer> subtaskId = new ArrayList<>();
       // for (Integer subtask : subtasks.keySet()) historyManager.remove(subtask);
        for (Integer i : subtasks.keySet()) {
            if (subtasks.get(i).getEpicId() == id) {
                subtaskId.add(i);
            }
        }
        for (Integer i = 0; i < subtaskId.size(); i++) {
            subtasks.remove(subtaskId.get(i));
            historyManager.remove(i);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        historyManager.remove(id);
        int epicId = subtasks.get(id).getEpicId();
        final Epic epic = epics.get(epicId);
        for (Subtask subtaskInEpic : epic.getSubtasks()) {
            if (subtaskInEpic.getId() == id) {
                epic.getSubtasks().remove(subtaskInEpic);
            }
        }
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) historyManager.remove(id);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) historyManager.remove(id);
        for (Integer id : subtasks.keySet()) historyManager.remove(id);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer id : subtasks.keySet()) historyManager.remove(id);
        subtasks.clear();
        for (int epicId : epics.keySet()) {
            epics.get(epicId).getSubtasks().clear();
            updateEpicStatus(epics.get(epicId));
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey((epic.getId()))) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        final Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        ArrayList<Subtask> subtaskByEpic = new ArrayList<>();
        final Epic epic = epics.get(epicId);
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
        Status status = null;

        for (Subtask subtaskId : epic.getSubtasks()) {
            status = subtaskId.getStatus();
            allSubtaskStatus++;
            if (status.equals(Status.NEW)) {
                allSubtaskNEW++;
            }
            if (status.equals(Status.DONE)) {
                allSubtaskDONE++;
            }
        }

        if (allSubtaskNEW == allSubtaskStatus) {
            epic.setStatus(Status.NEW);
        } else if (allSubtaskDONE == allSubtaskStatus) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Task getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
}
