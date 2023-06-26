package test;

import service.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends test.TaskManagerTest {
    public InMemoryTaskManagerTest() {
        manager = new InMemoryTaskManager();
    }
}
