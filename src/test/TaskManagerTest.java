package test;

import model.*;
import org.junit.jupiter.api.Test;
import service.ManagerValidateException;
import service.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {

    protected TaskManager manager;

    protected Task createNewTaskByTest() {
        return new Task("Test addNewTask", "Test addNewEpic description", Status.NEW);
    }

    protected Epic createNewEpicByTest() {
        return new Epic("Test addNewEpic", "Test addNewEpic description");
    }

    protected Subtask createNewSubtaskByTest(Epic epic) {
        return new Subtask(epic.getId(), "Test addNewEpic",
                "Test addNewEpic description",Status.NEW);
    }

    @Test
    public void createNewTask() {
        Task task = createNewTaskByTest();
        manager.createTask(task);
        assertNotNull(manager.getTaskById(1));
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void createNewEpic() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        assertNotNull(manager.getEpicById(1));
        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    public void createNewSubtask() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        Subtask subtask1 = createNewSubtaskByTest(epic);
        manager.createSubtask(subtask1);
        Subtask subtask2 = createNewSubtaskByTest(epic);
        manager.createSubtask(subtask2);

        assertNotNull(manager.getSubtaskById(2));
        assertEquals(epic.getId(), subtask1.getEpicId());

        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertEquals(subtask1, manager.getSubtaskById(2));
        assertEquals(List.of(subtask1, subtask2), subtasks);
    }

    @Test
    public void listOfPriorityTasks() {
        Task task2 = new Task("Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        Task task3 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW);
        Task task1 = new Task("Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");


        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task1);

        List<Task> tasksPriority = List.copyOf(manager.getPrioritizedTasks());
        List<Task> tasks = List.of(task1, task2, task3);

        assertEquals(tasksPriority, tasks);
    }

    @Test
    public void startAndEndTimeEpicDuration() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 55, "18:15 - 06.06.2023");
        Subtask subtask3  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "17:45 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        assertEquals(subtask1.getStartTime(),  epic.getStartTime());
        assertEquals(130, epic.getDuration());
        assertEquals(subtask2.getEndTime(), epic.getEndTime());
    }

    @Test
    public void validationTest() {
        Task task1 = new Task("Test addNewTask", "Tet addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task2 = new Task("Test Task", "Test addNewEpic description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        Task task3 = new Task("Test New", "Test addNewEpic description",
                Status.NEW, 30, "16:20 - 06.06.2023");
        Task task4 = new Task("Test New", "Test addNewEpic description",
                Status.NEW, 31, "15:30 - 06.06.2023");


        manager.createTask(task1);
        ManagerValidateException exception1 = assertThrows(ManagerValidateException.class, () -> {
            manager.createTask(task2);
        });
        ManagerValidateException exception2 = assertThrows(ManagerValidateException.class, () -> {
            manager.createTask(task3);
        });
        ManagerValidateException exception3 = assertThrows(ManagerValidateException.class, () -> {
            manager.createTask(task4);
        });
        assertEquals("В это время у вас уже есть задача: Test addNewTask", exception1.getMessage());
        assertEquals("В это время вы должны выполнять другую задачу: Test addNewTask", exception2.getMessage());
        assertEquals("В это время вы не успеете закончить эту задачу до начала задачи: Test addNewTask", exception3.getMessage());
    }

    @Test
    public void checkedStatusEpicIfAllSubtasksNew() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 55, "18:15 - 06.06.2023");
        Subtask subtask3  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "17:45 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkedStatusEpicIfAllSubtasksDone() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.DONE, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 55, "18:15 - 06.06.2023");
        Subtask subtask3  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 30, "17:45 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkedStatusEpicIfOneSubtaskDone() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.NEW, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 55, "18:15 - 06.06.2023");
        Subtask subtask3  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 30, "17:45 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void checkedStatusEpicIfDeleteNewSubtask() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.DONE, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 55, "18:15 - 06.06.2023");
        Subtask subtask3  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.DONE, 30, "17:45 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        manager.deleteSubtask(subtask2.getId());

        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertEquals(Status.DONE, epic.getStatus());
        assertEquals(List.of(subtask1, subtask3), subtasks);
    }

    @Test
    public void checkedStatusEpicIfNoSubtask() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void checkedStatusTaskIfUpdate() {
        Task task = createNewTaskByTest();
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);

        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void checkedStatusEpicIfUpdate() {
        Epic epic = createNewEpicByTest();

        manager.createEpic(epic);
        epic.setStatus(Status.DONE);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void checkedStatusEpicAndSubtaskIfUpdateSubtask() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.DONE, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 55, "18:15 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus());
        assertEquals(Status.DONE, subtask2.getStatus());
    }

    @Test
    public void deleteTaskById() {
        Task task = createNewTaskByTest();
        manager.createTask(task);
        manager.deleteTask(task.getId());

        assertEquals(new ArrayList<>() , manager.getAllTasks());
    }

    @Test
    public void deleteEpicById() {
        Epic epic = createNewEpicByTest();
        Subtask subtask1  = new Subtask(1 ,"Test addNeTask", "Tet addNewEpic description",
                Status.DONE, 45, "17:00 - 06.06.2023");
        Subtask subtask2  = new Subtask(1, "Test addNewTask", "Test addNewEpic description",
                Status.NEW, 55, "18:15 - 06.06.2023");

        manager.createEpic(epic);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        manager.deleteEpic(epic.getId());

        assertEquals(new ArrayList<>() , manager.getAllEpics());
        assertEquals(new ArrayList<>(), manager.getAllSubtasks());
    }

    @Test
    public void gettingAnEmptyTaskList() {
        Task task = createNewTaskByTest();
        manager.createTask(task);
        assertNull(manager.getTaskById(33));
        assertEquals(List.of(task), manager.getAllTasks());
    }

    @Test
    public void gettingAnEmptyEpicList() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        assertNull(manager.getEpicById(33));
        assertEquals(List.of(epic), manager.getAllEpics());
    }

    @Test
    public void gettingAnEmptySubtaskList() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        Subtask subtask = createNewSubtaskByTest(epic);
        manager.createSubtask(subtask);

        assertNull(manager.getSubtaskById(33));
        assertEquals(List.of(subtask), manager.getAllSubtasks());
    }

    @Test
    public void gettingAnEmptySubtaskListByEpicId() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);

        assertTrue(manager.getSubtaskByEpicId(epic.getId()).isEmpty());
    }

    @Test
    public void getEmptyTask() {
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void getEmptyEpic() {
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void getEmptySubtask() {
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void getEmptyTaskById() {
        assertNull(manager.getSubtaskById(33));
    }

    @Test
    public void getEmptyEpicById() {
        assertNull(manager.getSubtaskById(33));
    }

    @Test
    public void getEmptySubtaskById() {
        assertNull(manager.getSubtaskById(33));
    }

    @Test
    public void deleteATaskWithADifferentId() {
        Task task = createNewTaskByTest();
        manager.createTask(task);
        manager.deleteTask(33);

        assertEquals(List.of(task), manager.getAllTasks());
    }

    @Test
    public void deleteAEpicWithADifferentId() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        manager.deleteEpic(33);

        assertEquals(List.of(epic), manager.getAllEpics());
    }

    @Test
    public void deleteASubtaskWithADifferentId() {
        Epic epic = createNewEpicByTest();
        manager.createEpic(epic);
        Subtask subtask = createNewSubtaskByTest(epic);
        manager.createSubtask(subtask);
        manager.deleteSubtask(33);

        assertEquals(List.of(subtask), manager.getAllSubtasks());
    }

    @Test
    public void deleteATaskWithADifferentIdWithoutCreatingASingleTask() {
        manager.deleteTask(33);
        assertNull(manager.getTaskById(33));
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void deleteAEpicWithADifferentIdWithoutCreatingASingleEpic() {
        manager.deleteEpic(33);
        assertNull(manager.getEpicById(33));
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void deleteASubtaskWithADifferentIdWithoutCreatingASingleSubtask() {
        manager.deleteSubtask(33);
        assertNull(manager.getSubtaskById(33));
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void updateANonExistentTask() {
        Task task = createNewTaskByTest();
        manager.updateTask(task);
        assertNull(manager.getTaskById(1));
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void updateANonExistentEpic() {
        Epic epic = createNewEpicByTest();
        manager.updateEpic(epic);
        assertNull(manager.getEpicById(1));
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void updateANonExistentSubtask() {
        Epic epic = createNewEpicByTest();
        Subtask subtask = createNewSubtaskByTest(epic);
        manager.updateSubtask(subtask);
        assertNull(manager.getSubtaskById(1));
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void getEmptyHistoryAfterACall() {
        manager.getTaskById(33);
        manager.getEpicById(33);
        manager.getSubtaskById(33);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void getEmptyHistory() {
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void clearAllEmptyTask() {
        manager.deleteAllTasks();
        manager.deleteTask(33);

        assertEquals(new ArrayList<>(), manager.getAllTasks());
    }

    @Test
    public void clearAllEmptyEpic() {
        manager.deleteAllEpics();
        manager.deleteEpic(33);

        assertEquals(new ArrayList<>(), manager.getAllEpics());
    }

    @Test
    public void clearAllEmptySubtask() {
        manager.deleteAllSubtasks();
        manager.deleteSubtask(33);

        assertEquals(new ArrayList<>(), manager.getAllSubtasks());
    }

    @Test
    public void clearAllTasks() {
        Task task = createNewTaskByTest();
        Task task1 = createNewTaskByTest();

        manager.deleteAllTasks();

        assertEquals(new ArrayList<>(), manager.getAllTasks());
    }

    @Test
    public void clearAllEpic() {
        Epic epic = createNewEpicByTest();
        Epic epic1 = createNewEpicByTest();
        Subtask subtask = createNewSubtaskByTest(epic);
        Subtask subtask1 = createNewSubtaskByTest(epic);

        manager.deleteAllEpics();

        assertEquals(new ArrayList<>(), manager.getAllEpics());
        assertEquals(new ArrayList<>(), manager.getAllSubtasks());
    }

    @Test
    public void clearAllSubtask() {
        Epic epic = createNewEpicByTest();
        Subtask subtask = createNewSubtaskByTest(epic);
        Subtask subtask1 = createNewSubtaskByTest(epic);

        manager.deleteAllSubtasks();

        assertEquals(new ArrayList<>(), manager.getAllSubtasks());
    }

}