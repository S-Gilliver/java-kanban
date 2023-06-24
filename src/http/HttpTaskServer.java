package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final static int PORT = 8080;
    private TaskManager taskManager;
    private static HttpServer httpServer;
    private Gson gson;

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();
    }

    public static void start() {
        httpServer.start();
        System.out.println("Начало работы сервера на порту: " + PORT);
    }

    public static void stop() {
        httpServer.stop(0);
        System.out.println("Прекращение работы сервера на порту: " + PORT);
    }

    public HttpTaskServer() throws IOException {
        this.taskManager = Manager.getDefault();

        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(taskManager));
        httpServer.createContext("/tasks", new PrioritizedTaskHandler(taskManager));

        gson = Manager.getGson();
    }
}
