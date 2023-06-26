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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest  {

    protected TaskManager manager;
    KVServer kvServer;

    public HttpTaskManager createManager() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

    @BeforeEach
    public void taskManagerTest() {
        manager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void startServer() throws IOException {
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
    public void shouldLoadWithOneEmptyEpic() {
        Task task1 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task2 = new Task("Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        Epic epic2 = new Epic("Test NewEpic", "Test NewEpic description");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.getEpicById(1);
        manager.getEpicById(2);

        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, httpTaskManager.getAllEpics().size());
        assertEquals(2, httpTaskManager.getAllTasks().size());
        assertEquals(2, httpTaskManager.getHistory().size());
        assertTrue(httpTaskManager.getHistory().containsAll(httpTaskManager.getAllEpics()));
        assertEquals(0, httpTaskManager.getEpicById(0).getSubtasks().size());
    }

    @Test
    public void shouldLoadWithEmptyHistory() {
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
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        HttpTaskManager emptyHistoryManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, emptyHistoryManager.getAllEpics().size());
        assertEquals(2, emptyHistoryManager.getAllTasks().size());
        assertEquals(2, emptyHistoryManager.getAllSubtasks().size());
        assertEquals(0, emptyHistoryManager.getHistory().size());
    }

    @Test
    public void shouldLoadWithoutTimings() {
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
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(5);

        HttpTaskManager emptyHistoryManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, emptyHistoryManager.getAllEpics().size());
        assertEquals(2, emptyHistoryManager.getAllTasks().size());
        assertEquals(2, emptyHistoryManager.getAllSubtasks().size());
        assertEquals(4, emptyHistoryManager.getHistory().size());
    }

    @Test
    public void shouldHaveSameContent() {
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
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(5);

        HttpTaskManager standardManager = new HttpTaskManager("http://localhost:" + KVServer.PORT, true);

        assertEquals(2, standardManager.getAllEpics().size());
        assertEquals(2, standardManager.getAllTasks().size());
        assertEquals(2, standardManager.getAllSubtasks().size());
        assertEquals(3, standardManager.getHistory().size());
        assertTrue(standardManager.getAllTasks().contains(task1));
        assertTrue(standardManager.getAllTasks().contains(task2));
        assertEquals(2, standardManager.getEpicById(3).getSubtasks().size());
        assertEquals(0, standardManager.getEpicById(4).getSubtasks().size());
        assertTrue(standardManager.getEpicById(3).getSubtasks().contains(subtask1));
    }


}