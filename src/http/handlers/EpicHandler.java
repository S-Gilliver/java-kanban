package http.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import service.Manager;
import service.TaskManager;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EpicHandler extends handle implements HttpHandler {
    private final Gson gson = Manager.getGson();
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private int parsePathId(String strPath) {
        try {
            return Integer.parseInt(strPath);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET": {
                getEpic(httpExchange);
                break;
            }
            case "POST": {
                addEpic(httpExchange);
                break;
            }
            case "DELETE": {
                deleteEpic(httpExchange);
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Ожидали запрос GET, POST или DELETE, но получен запрос: " + method);
            }
        }
        httpExchange.close();
    }

    private void getEpic(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            String response = gson.toJson(taskManager.getAllEpics());
            System.out.println("GET EPICS: " + response);
            sendText(httpExchange, response);
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String response = gson.toJson(taskManager.getEpicById(id));
                sendText(httpExchange, response);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }

    private void addEpic(HttpExchange httpExchange) throws IOException {
        String request = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        try {
            Epic epic = gson.fromJson(request, Epic.class);
            int id = epic.getId();
            if (taskManager.getEpicById(id) != null) {
                taskManager.updateEpic(epic);
                httpExchange.sendResponseHeaders(200, 0);
                System.out.println("Успешное обновление epic по id " + id);
            } else {
                taskManager.createEpic(epic);
                httpExchange.sendResponseHeaders(200, 0);
                int epicId = epic.getId();
                System.out.println("Epic успешно добавлен по id" + epicId);
            }
        } catch (JsonSyntaxException ex) {
            httpExchange.sendResponseHeaders(400,0);
            System.out.println("Введен неправильный формат запроса");
        }
    }

    private void deleteEpic(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteAllEpics();
            httpExchange.sendResponseHeaders(200, 0);
            System.out.println("Удаление всех эпиков прошло успешно");
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                taskManager.deleteEpic(id);
                System.out.println("Эпик с id " + id + " успешно удален!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }
}
