package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Manager.getDefaultHistory();
    protected int generatedId = 1;

    protected final Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if ((o1.getStartTime() == null) && (o2.getStartTime() == null)) {
            return o1.getId() - o2.getId();
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    });


    @Override
    public void createTask(Task task) {
        validateTaskPriority(task);
        task.setId(generatedId++);
        tasks.put(task.getId(), task);
        addPrioritizedTasks(task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generatedId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        validateTaskPriority(subtask);
        subtask.setId(generatedId++);
        final Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        addPrioritizedTasks(subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
        epic.calculateStartTime();
        epic.calculateDuration();
        epic.calculateEndTime();
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.get(id) != null) {
            Epic epic = epics.remove(id);
            for (Subtask subTask : epic.getSubtasks()) {
                prioritizedTasks.remove(subTask);
                subtasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtask(Integer id) {
        if (subtasks.get(id) != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
            Subtask subtask = subtasks.remove(id);
            int epicId = subtask.getEpicId();
            final Epic epic = epics.get(epicId);
            epic.getSubtasks().remove(subtask);
            updateEpicStatus(epic);
            epic.calculateStartTime();
            epic.calculateDuration();
            epic.calculateEndTime();
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) historyManager.remove(id);
        getAllTasks().forEach(prioritizedTasks::remove);
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
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        getAllSubtasks().forEach(prioritizedTasks::remove);
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateTask(Task task) {
        validateTaskPriority(task);
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            prioritizedTasks.removeIf(task1 -> task1.getId() == task.getId());
            addPrioritizedTasks(task);
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
    public void updateSubtask(Subtask subtask) {
        validateTaskPriority(subtask);
        addPrioritizedTasks(subtask);
        final Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().remove(subtasks.put(subtask.getId(), subtask));
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
        epic.calculateStartTime();
        epic.calculateDuration();
        epic.calculateEndTime();
        prioritizedTasks.removeIf(task1 -> task1.getId() == subtask.getId());
        prioritizedTasks.add(subtask);
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(int epicId) {
        final Epic epic = epics.get(epicId);
        return new ArrayList<>(epic.getSubtasks());
    }

    private void updateEpicStatus(Epic epic) {
        //(проверка на) изменение статуса епика после удаления или добавления подзадачи (subtask)
        int allSubtaskNEW = 0;
        int allSubtaskDONE = 0;
        int size = epic.getSubtasks().size();
        Status status;

        for (Subtask subtaskId : epic.getSubtasks()) {
            status = subtaskId.getStatus();
            if (status.equals(Status.NEW)) {
                allSubtaskNEW++;
            } else if (status.equals(Status.DONE)) {
                allSubtaskDONE++;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }

        if (allSubtaskNEW == size) {
            epic.setStatus(Status.NEW);
        } else if (allSubtaskDONE == size) {
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
    public Epic getEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void addPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void validateTaskPriority(Task task) {
            for (Task checkedTask : prioritizedTasks) {
                if (task.getStartTime() == null || checkedTask.getStartTime() == null) continue;
                if(task.getId() == checkedTask.getId() &&
                        (task.getStartTime().isEqual(checkedTask.getStartTime()) ||
                                task.getStartTime().isAfter(checkedTask.getStartTime()) &&
                                        task.getStartTime().isBefore(checkedTask.getEndTime()))) continue;

                if (task.getStartTime().isEqual(checkedTask.getStartTime())) {
                    throw new ManagerValidateException("В это время у вас уже есть задача: "
                            + checkedTask.getTaskName());
                } else if (task.getStartTime().isAfter(checkedTask.getStartTime()) &&
                        task.getStartTime().isBefore(checkedTask.getEndTime())) {
                    throw new ManagerValidateException("В это время вы должны выполнять другую задачу: "
                            + checkedTask.getTaskName());
                } else if (task.getEndTime().isAfter(checkedTask.getStartTime()) &&
                        task.getEndTime().isBefore(checkedTask.getEndTime())) {
                    throw new ManagerValidateException("В это время вы не успеете закончить эту задачу до начала задачи: "
                            + checkedTask.getTaskName());
                }
            }
        }
    }

