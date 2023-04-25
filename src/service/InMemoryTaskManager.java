package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();
    private int generatedId = 1;


    @Override
    public void createTask(Task task) {
        task.setId(generatedId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generatedId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generatedId++);
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
        Epic epic = epics.remove(id);
        for (Subtask subTask : epic.getSubtasks()) {
            subtasks.remove(subTask);
            historyManager.remove(subTask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        historyManager.remove(id);
        Subtask subtask = subtasks.remove(id);
        int epicId = subtask.getEpicId();
        final Epic epic = epics.get(epicId);
        epic.getSubtasks().remove(subtask);
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
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
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
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setTaskName(epic.getTaskName());
            oldEpic.setTaskDescription(epic.getTaskDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, int idSubtask) {
        final Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        deleteSubtask(idSubtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(int epicId) {
        List<Subtask> subtaskByEpic = new ArrayList<>();
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
    public List<Task> getAllTasks() {
        return List.copyOf(tasks.values());

    }

    @Override
    public List<Epic> getAllEpics() {
        return List.copyOf(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return List.copyOf(subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        final Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Task getEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Task getSubtaskById(int id) {
        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }
}
