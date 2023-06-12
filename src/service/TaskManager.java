package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask);

    void deleteTask(int id);
    void deleteEpic(int id);
    void deleteSubtask(Integer id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    List<Subtask> getSubtaskByEpicId(int epicId);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);

    List<Task> getHistory();

    public void addPrioritizedTasks(Task task);

    public Set<Task> getPrioritizedTasks();
}
