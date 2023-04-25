import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Manager;
import service.TaskManager;




public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Manager.getDefault();


        Task task1 = new Task("Дом", "убрать квартиру", Status.NEW);
        Task task2 = new Task("Хобби", "нарисовать картину", Status.NEW);
        Task task3 = new Task("Отдых", "пойти на каток", Status.NEW);
        Task task4 = new Task("Домашние животные", "наложить еды", Status.NEW);

        Epic epic1 = new Epic("Пойти в рестораны", "кувовов");
        Epic epic2 = new Epic("животное   ывыыв", "сыр выыв");
        Epic epic3 = new Epic("машина", "запчасти");

        Subtask subtask1 = new Subtask(3, "qwe", "ffffff", Status.NEW);
        Subtask subtask2 = new Subtask(3, "qwууe", "fffууfff", Status.DONE);
        Subtask subtask3 = new Subtask(3, "Pay", "Machine", Status.NEW);
        Subtask subtask4 = new Subtask(3, "wendy", "solo", Status.NEW);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.createSubtask(subtask3);
        inMemoryTaskManager.createEpic(epic2);

        System.out.println(inMemoryTaskManager.getTaskById(1));
        System.out.println(inMemoryTaskManager.getEpicById(3));
        System.out.println(inMemoryTaskManager.getSubtaskByEpicId(3));
        System.out.println(inMemoryTaskManager.getEpicById(7));

        System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.updateSubtask(subtask4, 4);
        System.out.println(inMemoryTaskManager.getAllEpics());

        /*
        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.deleteTask(1);

        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.deleteEpic(3);

        System.out.println(inMemoryTaskManager.getHistory());


         */
        //manager.createSubtask(subtask4);


        //System.out.println(manager.getSubtaskByEpicId(2));

        //subtask3.setId(2);
        //manager.updateSubtask(subtask3);
        //System.out.println(inMemoryTaskManager.getAllEpics());

        //inMemoryTaskManager.deleteEpic(2);
        //inMemoryTaskManager.deleteSubtask(3);
        //inMemoryTaskManager.deleteAllSubtasks();


        //System.out.println(inMemoryTaskManager.getTaskById(1));
        //inMemoryTaskManager.getEpicById(2);
        //inMemoryTaskManager.getTaskById(1);

        //System.out.println(inMemoryTaskManager.getHistory());
     /*
            System.out.println(inMemoryTaskManager.getSubtaskById(4));
            System.out.println(inMemoryTaskManager.getAllSubtasks());
            System.out.println(inMemoryTaskManager.getSubtaskByEpicId(2));
            System.out.println(inMemoryTaskManager.getAllTasks());
            System.out.println(inMemoryTaskManager.getTaskById(1));

      */
      //  System.out.println(inMemoryTaskManager.getAllEpics());
        // System.out.println(inMemoryTaskManager.getSubtaskByEpicId(2));

        //     inMemoryTaskManager.deleteAllTask();
        //     inMemoryTaskManager.deleteTask(1);
        //    inMemoryTaskManager.deleteAllEpic();

        //   System.out.println(inMemoryTaskManager.getSubtaskById(4));
        //   System.out.println(inMemoryTaskManager.getAllSubtasks());
        //   System.out.println(inMemoryTaskManager.getAllTasks());

        //   task3.setId(5);
        //   inMemoryTaskManager.updateTask(task3);
        //   System.out.println(inMemoryTaskManager.getTaskById(5));
        //  System.out.println(inMemoryTaskManager.getSubtaskByEpicId(2));

        //   System.out.println(inMemoryTaskManager.getSubtaskByEpicId(1));
        //    System.out.println(inMemoryTaskManager.getTaskById(1));
        //    manager.deleteAllTask();
        //     manager.deleteTask(1);
        //    System.out.println(inMemoryTaskManager.getAllTasks());


    }


}
