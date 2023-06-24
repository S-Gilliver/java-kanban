package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;
import service.FileBackedTasksManager;
import service.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient httpClient;
    private final Gson gson;
    public HttpTaskManager(String url) {
        super(new File(url));
        try {
            httpClient = new KVTaskClient(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gson = Manager.getGson();
    }

    public void loadFromKVServer() throws IOException, InterruptedException {
        String taskJson = httpClient.load("tasks");
        String epicJson = httpClient.load("epics");
        String subTaskJson = httpClient.load("subTasks");
        String history = httpClient.load("history");

        List<Task> tasks = gson.fromJson((taskJson), new TypeToken<ArrayList<Task>>() {}.getType());
        if (tasks != null) {
            tasks.forEach(this::createTask);
        }
        List<Epic> epics = gson.fromJson((epicJson), new TypeToken<ArrayList<Epic>>() {}.getType());
        if (epics != null) { //сначала тащим эпик без подзадач, затем подзадачи
            epics.forEach((epic) -> {
                epic.getSubtasks().clear();
                super.createEpic(epic);
            });
        }
        List<Subtask> subTasks = gson.fromJson((subTaskJson), new TypeToken<ArrayList<Subtask>>() {}.getType());
        if (subTasks != null) {
            subTasks.forEach(this::createSubtask);
        }
        List<Integer> historyMemory = gson.fromJson((history), new TypeToken<ArrayList<Integer>>() {}.getType());
        historyMemory.forEach((task) -> {
            switch (TaskType.valueOf(task.getClass().getSimpleName().toUpperCase())) {
                case TASK:
                    super.getTaskById(task);
                    break;
                case EPIC:
                    super.getEpicById(task);
                    break;
                case SUBTASK:
                    super.getSubtaskById(task);
                    break;
            }
        });
    }
}
