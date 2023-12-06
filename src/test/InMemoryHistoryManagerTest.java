package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryHistoryManagerTest {
    private HistoryManager manager;

    protected Task createNewTaskByTest() {
        return new Task("Test addNewTask", "Test addNewEpic description", Status.NEW);
    }

    public InMemoryHistoryManager createManager() {
        return new InMemoryHistoryManager();
    }

    @BeforeEach
    public void updateManager() {
        manager = createManager();
    }

    @Test
    public void addHistory() {
        Task task = createNewTaskByTest();
        manager.add(task);

        assertNotNull(manager);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void addDuplicateHistory() {
        Task task = createNewTaskByTest();
        manager.add(task);
        manager.add(task);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void removeTaskFromHistory() {
        Task task = createNewTaskByTest();
        manager.add(task);
        manager.remove(task.getId());

        assertEquals(new ArrayList<>(), manager.getHistory());

    }

    @Test
    public void removeTaskFromHistoryHead() {
        Task task1 = new Task(1 ,"Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Task task2 = new Task(2,"Test addNew", "Test addNewEpic description",
                Status.NEW, 45, "19:00 - 06.06.2023");
        Task task3 = new Task(3,"Test NewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task1.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals(task2, manager.getHistory().get(0));
        assertEquals(task3, manager.getHistory().get(1));
    }

    @Test
    public void removeTaskFromHistoryCenter() {
        Task task1 = new Task(1 ,"Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Task task2 = new Task(2,"Test addNew", "Test addNewEpic description",
                Status.NEW, 45, "19:00 - 06.06.2023");
        Task task3 = new Task(3,"Test NewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task3, manager.getHistory().get(1));
    }

    @Test
    public void removeTaskFromHistoryTail() {
        Task task1 = new Task(1 ,"Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Task task2 = new Task(2,"Test addNew", "Test addNewEpic description",
                Status.NEW, 45, "19:00 - 06.06.2023");
        Task task3 = new Task(3,"Test NewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task3.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
    }

    @Test
    public void emptyTaskHistory() {
        assertTrue(manager.getHistory().isEmpty());
    }
}
