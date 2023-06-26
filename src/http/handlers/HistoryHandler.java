package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends handle implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String query = httpExchange.getRequestURI().getQuery();
        String path = httpExchange.getRequestURI().getPath();

        System.out.println("Идет обработка запроса " + path + " с помощью метода " + method);

        if (("GET").equals(method)) {
            if (query == null) {
                String response = gson.toJson(taskManager.getHistory());
                System.out.println("GET History: " + response);
                sendText(httpExchange, response);
            }
        } else {
            httpExchange.sendResponseHeaders(405, 0);
            System.out.println("Ожидали запрос GET, но получили: " + method);
        }
        httpExchange.close();
    }
}