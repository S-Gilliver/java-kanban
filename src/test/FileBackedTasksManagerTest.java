package test;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;


import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    @BeforeEach
    public void updateFileBackedTaskManager() {
        File file = new File("tasks.csv");
        manager = new FileBackedTasksManager(file);
    }

    @Test
    public void loadFromFileTask() {
        Task task1 = new Task(1 ,"Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Task task2 = new Task(2,"Test addNew", "Test addNewEpic description",
                Status.NEW, 45, "19:00 - 06.06.2023");
        Task task3 = new Task(3,"Test NewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.getTaskById(task2.getId());
        manager.getTaskById(task2.getId());

        FileBackedTasksManager result = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));

        assertEquals(3, result.getAllTasks().size());
        assertEquals(manager.getAllTasks(), result.getAllTasks());
        assertEquals(manager.getHistory(), result.getHistory());
    }

    @Test
    public void loadFromFileEpicAndSubtask() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW);
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW);

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.getSubtaskById(subtask2.getId());
        manager.getSubtaskById(subtask1.getId());

        FileBackedTasksManager result = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));

        assertEquals(2, result.getHistory().size());
        assertEquals(manager.getAllEpics(), result.getAllEpics());
        assertEquals(manager.getAllSubtasks(), result.getAllSubtasks());
    }

    @Test
    public void loadFromEmptyFile() {
        Task task1 = new Task(1 ,"Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(2 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW);

        manager.createTask(task1);
        manager.createEpic(epic);
        manager.createSubtask(subtask1);

        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();

        FileBackedTasksManager result = FileBackedTasksManager.loadFromFile(new File("tasks.csv"));

        assertEquals(0, result.getAllTasks().size());
        assertEquals(0, result.getAllEpics().size());
        assertEquals(0, result.getAllSubtasks().size());
        assertEquals(0, result.getHistory().size());
    }

}
