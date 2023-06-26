package http.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;
import service.Manager;
import service.TaskManager;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SubtaskHandler extends handle implements HttpHandler {

    private final Gson gson = Manager.getGson();
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                getSubtask(httpExchange);
                break;
            }
            case "POST": {
                addSubtask(httpExchange);
                break;
            }
            case "DELETE": {
                deleteSubtask(httpExchange);
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Ожидали запрос GET, POST или DELETE, но получен запрос: " + method);
            }
        }
        httpExchange.close();
    }

    private void getSubtask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            String response = gson.toJson(taskManager.getAllSubtasks());
            System.out.println("GET SUBTASKS: " + response);
            sendText(httpExchange, response);
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String response = gson.toJson(taskManager.getSubtaskById(id));
                sendText(httpExchange, response);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }

    }

    private void addSubtask(HttpExchange httpExchange) throws IOException {
        String request = new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
        try {
            Subtask subTask = gson.fromJson(request, Subtask.class);
            int id = subTask.getId();
            if (taskManager.getSubtaskById(id) != null) {
                taskManager.updateSubtask(subTask);
                httpExchange.sendResponseHeaders(200, 0);
                System.out.println("Успешное обновление подзадачи по id " + id);
            } else {
                taskManager.createSubtask(subTask);
                httpExchange.sendResponseHeaders(200, 0);
                int subTaskId = subTask.getId();
                System.out.println("Подзадача успешно добавлена по id" + subTaskId);
            }
        } catch (JsonSyntaxException ex) {
            httpExchange.sendResponseHeaders(400,0);
            System.out.println("Введен неправильный формат запроса");
        }
    }

    private void deleteSubtask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteAllSubtasks();
            httpExchange.sendResponseHeaders(200, 0);
            System.out.println("Удаление всех подзадач прошло успешно");
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                taskManager.deleteSubtask(id);
                System.out.println("Задача с id " + id + " успешно удалена!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }
}
