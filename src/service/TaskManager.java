package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

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
    void updateSubtask(Subtask subtask, int id);

    List<Subtask> getSubtaskByEpicId(int epicId);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    Task getTaskById(int id);
    Task getEpicById(int id);
    Task getSubtaskById(int id);

    List<Task> getHistory();
}
