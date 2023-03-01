import model.Epic;
import model.Subtask;
import model.Task;
import service.Manager;


public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Дом", "убрать квартиру", "NEW");
        Task task2 = new Task("Хобби", "нарисовать картину", "NEW");
        Task task3 = new Task("Отдых", "пойти на каток", "NEW");
        Task task4 = new Task("Домашние животные", "наложить еды", "NEW");

        Epic epic1 = new Epic("Пойти в рестораны", "кувовов");
        Epic epic2 = new Epic("животное   ывыыв", "сыр выыв");
        Epic epic3 = new Epic("машина", "запчасти");

        Subtask subtask1 = new Subtask(2, "qwe", "ffffff", "NEW");
        Subtask subtask2 = new Subtask(2, "qwууe", "fffууfff", "DONE");
        Subtask subtask3 = new Subtask(2, "Pay", "Machine", "NEW");
        Subtask subtask4 = new Subtask(2, "wendy", "solo", "NEW");

        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        //manager.createSubtask(subtask4);
        manager.createTask(task2);

        // System.out.println(manager.getSubtaskByEpicId(2));

       // subtask3.setId(2);
       // manager.updateSubtask(subtask3);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

       // manager.deleteEpic(2);
        manager.deleteSubtask(3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        System.out.println();
     /*
            System.out.println(manager.getSubtaskById(4));
            System.out.println(manager.getAllSubtasks());
            System.out.println(manager.getSubtaskByEpicId(2));
            System.out.println(manager.getAllTasks());
            System.out.println(manager.getTaskById(1));

      */
      //  System.out.println(manager.getAllEpics());
        // System.out.println(manager.getSubtaskByEpicId(2));

        //     manager.deleteAllTask();
        //     manager.deleteTask(1);
        //    manager.deleteAllEpic();

        //   System.out.println(manager.getSubtaskById(4));
        //   System.out.println(manager.getAllSubtasks());
        //   System.out.println(manager.getAllTasks());

        //   task3.setId(5);
        //   manager.updateTask(task3);
        //   System.out.println(manager.getTaskById(5));
        //  System.out.println(manager.getSubtaskByEpicId(2));

        //   System.out.println(manager.getSubtaskByEpicId(1));
        //    System.out.println(manager.getTaskById(1));
        //    manager.deleteAllTask();
        //     manager.deleteTask(1);
        //    System.out.println(manager.getAllTasks());


    }


}
