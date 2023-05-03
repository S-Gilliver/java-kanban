package service;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                fileWriter.write(toString(task));
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(toString(epic));
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(toString(subtask));
            }
            fileWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                if (line.equals("")) {
                    break;
                }
                Task task = fromString(line);
                if (task == null) {
                    List<Integer> history = historyFromString(line);
                    for (Integer hist : history) {
                        manager.getTaskById(hist);
                        manager.getEpicById(hist);
                        manager.getSubtaskById(hist);
                    }
                    break;
                }
                switch (task.getClass().getSimpleName().toUpperCase()) {
                    case ("TASK"):
                        manager.createTask(task);
                        break;
                    case ("EPIC"):
                        manager.createEpic((Epic) task);
                        break;
                    case ("SUBTASK"):
                        manager.createSubtask((Subtask) task);
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return manager;
    }

    private static Task fromString(String value) {
        List<String> tasks = List.of(value.split(","));
        switch (tasks.get(1)) {
            case "TASK":
                Task task = new Task(tasks.get(2), tasks.get(4));
                task.setStatus(Status.valueOf(tasks.get(3)));
                task.setId(Integer.parseInt(tasks.get(0)));
                return task;
            case "EPIC":
                Epic epic = new Epic(tasks.get(2), tasks.get(4));
                epic.setStatus(Status.valueOf(tasks.get(3)));
                epic.setId(Integer.parseInt(tasks.get(0)));
                return epic;
            case "SUBTASK":
                return new Subtask(Integer.parseInt(tasks.get(5)), tasks.get(2)
                        , tasks.get(4), Status.valueOf(tasks.get(3)));
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(int epicId) {
        List<Subtask> list = super.getSubtaskByEpicId(epicId);
        save();
        return list;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> list = super.getAllTasks();
        save();
        return list;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> list = super.getAllEpics();
        save();
        return list;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> list = super.getAllSubtasks();
        save();
        return list;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    private String toString(Task task) {
        return task.getId() + ","
                + TaskType.TASK + ","
                + task.getTaskName() + ","
                + task.getStatus() + ","
                + task.getTaskDescription() + "\n";
    }

    private String toString(Epic epic) {
        return epic.getId() + ","
                + TaskType.EPIC + ","
                + epic.getTaskName() + ","
                + epic.getStatus() + ","
                + epic.getTaskDescription() + "\n";
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + ","
                + TaskType.SUBTASK + ","
                + subtask.getTaskName() + ","
                + subtask.getStatus() + ","
                + subtask.getTaskDescription() + ","
                + subtask.getEpicId() + "\n";
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder str = new StringBuilder();
        for (Task task : manager.getHistory()) {
            str.append(task.getId()).append(",");
        }
        if (str.length() > 0) {
            str = new StringBuilder(str.substring(0, str.length() - 1) + "\n");
        }
        return str.toString();


    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        List<String> tasks = List.of(value.split(","));
        for (String id : tasks) {
            list.add(Integer.valueOf(id));
        }
        return list;
    }
}
