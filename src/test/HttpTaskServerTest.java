import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import http.KVServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static KVServer kvServer;
    private static Gson gson;
    private static HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private static final String PRIORITY_TASK_PATH = "http://localhost:8080/tasks";
    private static final String TASK_PATH = "http://localhost:8080/tasks/task";
    private static final String EPIC_PATH = "http://localhost:8080/tasks/epic";
    private static final String SUBTASK_PATH = "http://localhost:8080/tasks/subtask";
    private static final String HISTORY_PATH = "http://localhost:8080/tasks/history";
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Epic epic3;
    Subtask subTask1;


    private void createTasks() {
        task1 = new Task("Test addNeTask", "Tet addNewTask description",
                Status.NEW, 30, "16:55 - 06.06.2023");
        task2 = new Task("Test addNewTask", "Test NewTask description",
                Status.NEW, 30, "16:00 - 06.06.2023");
        epic1 = new Epic("Test addNewEpic", "Tet addNewEpic description");
        epic2 = new Epic("Test addEpic", "Tet NewEpic description");
        epic3 = new Epic("Test NewEpic", "Tet addEpic description");
        subTask1 = new Subtask(3 ,"Test addNewSubtask", "Tet addNewSubtask description",
                Status.NEW, 45, "18:00 - 06.06.2023");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subTask1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getAllTasks();
    }

    @BeforeAll
    static void start() {
        gson = Manager.getGson();
        kvServer = new KVServer();
        kvServer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Manager.getDefault();
        httpTaskServer = new HttpTaskServer();
        createTasks();

        httpTaskServer.start();
    }

    @AfterEach
    public void tearDown() {
        httpTaskServer.stop();
    }

    @AfterAll
    static void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void postTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(TASK_PATH);

        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(taskJson1);
        HttpRequest.BodyPublisher bodyJson2 = HttpRequest.BodyPublishers.ofString(taskJson2);

        HttpRequest request1 = HttpRequest.newBuilder().POST(bodyJson1).uri(uri).build();
        HttpRequest request2 = HttpRequest.newBuilder().POST(bodyJson2).uri(uri).build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());

    }

    @Test
    void getTaskResponseTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(TASK_PATH);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

    }

    @Test
    void deleteTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(TASK_PATH + "?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(TASK_PATH);
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    @Test
    void postEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(EPIC_PATH);
        String epicJson1 = gson.toJson(epic1);
        String epicJson2 = gson.toJson(epic2);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(epicJson1);
        HttpRequest.BodyPublisher bodyJson2 = HttpRequest.BodyPublishers.ofString(epicJson2);

        HttpRequest request1 = HttpRequest.newBuilder().POST(bodyJson1).uri(uri).build();
        HttpRequest request2 = HttpRequest.newBuilder().POST(bodyJson2).uri(uri).build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
    }
    @Test
    void deleteEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(EPIC_PATH + "?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void deleteAllEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(EPIC_PATH);
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void postSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri1 = URI.create(EPIC_PATH);
        URI uri2 = URI.create(SUBTASK_PATH);

        String epicJson = gson.toJson(epic1);
        String subtaskJson = gson.toJson(subTask1);

        HttpRequest.BodyPublisher bodyJson1 = HttpRequest.BodyPublishers.ofString(epicJson);
        HttpRequest.BodyPublisher bodyJson2 = HttpRequest.BodyPublishers.ofString(subtaskJson);

        HttpRequest request1 = HttpRequest.newBuilder().POST(bodyJson1).uri(uri1).build();
        HttpRequest request2 = HttpRequest.newBuilder().POST(bodyJson2).uri(uri2).build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
    }
    @Test
    void getSubtaskResponseTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(SUBTASK_PATH);
        taskManager.getSubtaskById(subTask1.getId());
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

    }
    @Test
    void deleteAllSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(SUBTASK_PATH);
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteSubtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(SUBTASK_PATH + "?id=" + subTask1.getId());
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void getEmptyHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(HISTORY_PATH);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNotNull(gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType()));
        assertEquals(response.body(), "[]");
    }

    @Test
    void getEmptyPriorityTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PRIORITY_TASK_PATH);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType()));
        assertEquals(response.body(), "[]");
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(HISTORY_PATH);

        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(response.body(), gson.toJson(List.of(task1, task2)));
    }

    @Test
    void getPriorityTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(PRIORITY_TASK_PATH);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(response.body(), gson.toJson(taskManager.getPrioritizedTasks()));
    }
}