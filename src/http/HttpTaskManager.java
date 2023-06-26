package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;
import service.FileBackedTasksManager;
import service.InMemoryHistoryManager;
import service.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    public final KVTaskClient httpClient;
    private final Gson gson;
    final static String KEY_TASKS = "tasks";
    final static String KEY_EPICS = "epics";
    final static String KEY_SUBTASKS = "subTasks";
    final static String KEY_HISTORY = "history";

    InMemoryHistoryManager inMemoryHistoryManager;
    public HttpTaskManager(String url, Boolean load) {
        super(new File(url));
        httpClient = new KVTaskClient(url);
        gson = Manager.getGson();
        if (load) {
            loadFromKVServer();
        }
    }

    public HttpTaskManager(String url) {
        super(new File(url));
        httpClient = new KVTaskClient(url);
        gson = Manager.getGson();
    }

    @Override
    public void save() {
        String task = gson.toJson(tasks.values());
        String epic = gson.toJson(epics.values());
        String subTask = gson.toJson(subtasks.values());
        String history = gson.toJson(historyManager.getHistory().stream().map(Task::getId)
                .collect(Collectors.toList()));
            httpClient.put(KEY_TASKS, task);
            httpClient.put(KEY_EPICS, epic);
            httpClient.put(KEY_SUBTASKS, subTask);
            httpClient.put(KEY_HISTORY, history);
        }


    private void loadFromKVServer() {
        String taskJson = httpClient.load(KEY_TASKS);
        String epicJson = httpClient.load(KEY_EPICS);
        String subTaskJson = httpClient.load(KEY_SUBTASKS);
        String history = httpClient.load(KEY_HISTORY);

        List<Task> tasksList = gson.fromJson((taskJson), new TypeToken<ArrayList<Task>>() {}.getType());
        if (tasksList != null) {
            for (Task t : tasksList) {
                tasks.put(t.getId(), t);
            }
        }
        List<Epic> epicsList = gson.fromJson((epicJson), new TypeToken<ArrayList<Epic>>() {}.getType());
        if (epicsList != null) {
            epicsList.forEach((epic) -> {
                epic.getSubtasks().clear();
                epics.put(epicsList.get(0).getId(), (Epic) epicsList);
            });
        }
        List<Subtask> subTasksList = gson.fromJson((subTaskJson), new TypeToken<ArrayList<Subtask>>() {}.getType());
        if (subTasksList != null) {
            for (Subtask s : subTasksList) {
                subtasks.put(s.getId(), s);
            }
        }
        inMemoryHistoryManager = new InMemoryHistoryManager();
        List<Integer> historyMemory = gson.fromJson((history), new TypeToken<ArrayList<Integer>>() {}.getType());
        historyMemory.forEach((task) -> {
            switch (TaskType.valueOf(task.getClass().getSimpleName().toUpperCase())) {
                case TASK:
                    inMemoryHistoryManager.add(tasks.get(task));
                    break;
                case EPIC:
                    inMemoryHistoryManager.add(epics.get(task));
                    break;
                case SUBTASK:
                    inMemoryHistoryManager.add(subtasks.get(task));
                    break;
            }
        });
    }
}
