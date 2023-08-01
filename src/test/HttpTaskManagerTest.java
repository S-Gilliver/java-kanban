package test;

import http.HttpTaskManager;
import http.KVServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends test.FileBackedTasksManagerTest {

    protected TaskManager manager;
    KVServer kvServer;

    @BeforeEach
    public void taskManagerTest() {
        manager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void startServer() {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }

    @Test
    public void shouldLoadFromEmptyServer() {
        HttpTaskManager emptyManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(0, emptyManager.getAllTasks().size());
        assertEquals(0, emptyManager.getAllSubtasks().size());
        assertEquals(0, emptyManager.getAllEpics().size());
        assertEquals(0, emptyManager.getHistory().size());
        assertNotNull(emptyManager);
    }

    @Test
    public void shouldLoadWithEmptyHistory() {
        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        Task task1 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task2 = new Task("Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        Epic epic2 = new Epic("Test NewEpic", "Test NewEpic description");
        Subtask subtask1  = new Subtask(3 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 07.06.2023");
        Subtask subtask2  = new Subtask(3, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 55, "18:15 - 08.06.2023");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, taskManager2.getAllEpics().size());
        assertEquals(2, taskManager2.getAllTasks().size());
        assertEquals(2, taskManager2.getAllSubtasks().size());
        assertEquals(0, taskManager2.getHistory().size());
    }

    @Test
    public void shouldLoadWithoutTimings() {
        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        Task task1 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task2 = new Task("Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        Epic epic2 = new Epic("Test NewEpic", "Test NewEpic description");
        Subtask subtask1  = new Subtask(3 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 07.06.2023");
        Subtask subtask2  = new Subtask(3, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 55, "18:15 - 08.06.2023");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(5);

        HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, taskManager2.getAllEpics().size());
        assertEquals(2, taskManager2.getAllTasks().size());
        assertEquals(2, taskManager2.getAllSubtasks().size());
        assertEquals(3, taskManager2.getHistory().size());
    }

    @Test
    public void shouldHaveSameContent() {
        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        Task task1 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task2 = new Task("Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        Epic epic2 = new Epic("Test NewEpic", "Test NewEpic description");
        Subtask subtask1  = new Subtask(3 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 07.06.2023");
        Subtask subtask2  = new Subtask(3, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 55, "18:15 - 08.06.2023");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(5);

        HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, taskManager2.getAllEpics().size());
        assertEquals(2, taskManager2.getAllTasks().size());
        assertEquals(2, taskManager2.getAllSubtasks().size());
        assertEquals(3, taskManager2.getHistory().size());
        assertTrue(taskManager2.getAllTasks().contains(task1));
        assertTrue(taskManager2.getAllTasks().contains(task2));
        assertEquals(2, taskManager2.getEpicById(3).getSubtasks().size());
        assertEquals(0, taskManager2.getEpicById(4).getSubtasks().size());
        assertTrue(taskManager2.getEpicById(3).getSubtasks().contains(subtask1));
    }
}