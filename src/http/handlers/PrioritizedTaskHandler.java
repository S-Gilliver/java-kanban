package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedTaskHandler extends Handle {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedTaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        String path = httpExchange.getRequestURI().getPath();

        System.out.println("Идет обработка запроса " + path + " с помощью метода " + method);

        if (("GET").equals(method)) {
            if (query == null) {
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                System.out.println("GET TASKS: " + response);
                sendText(httpExchange, response);
            }
        } else {
            httpExchange.sendResponseHeaders(405, 0);
            System.out.println("Ожидали запрос GET, но получили: " + method);
        }

        httpExchange.close();
    }
}
