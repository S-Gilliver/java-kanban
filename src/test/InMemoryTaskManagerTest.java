package test;

import service.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    public InMemoryTaskManagerTest() {
        manager = new InMemoryTaskManager();
    }
}
