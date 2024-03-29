package http.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;


public class TaskHandler extends Handle  {
    private final Gson gson;
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
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
        System.out.println(method);
        switch (method) {
            case "GET": {
                getTask(httpExchange);
                break;
            }
            case "POST": {
                addTask(httpExchange);
                break;
            }
            case "DELETE": {
                deleteTask(httpExchange);
                break;
            }
            default: {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Ожидали запрос GET, POST или DELETE, но получен запрос: " + method);
            }
        }
        httpExchange.close();
    }

    private void getTask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            String response = gson.toJson(taskManager.getAllTasks());
            System.out.println("GET TASKS: " + response);
            sendText(httpExchange, response);
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                String response = gson.toJson(taskManager.getTaskById(id));
                sendText(httpExchange, response);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }

    private void addTask(HttpExchange httpExchange) throws IOException {
        String request = readText(httpExchange);
        if (request.isEmpty()) {
            httpExchange.sendResponseHeaders(400,0);
            return;
        }
        try {
            Task task = gson.fromJson(request, Task.class);
            int id = task.getId();
            if (taskManager.getTaskById(id) != null) {
                taskManager.updateTask(task);
                httpExchange.sendResponseHeaders(200, 0);
                System.out.println("Успешное обновление задачи по id " + id);
            } else {
                taskManager.createTask(task);
                httpExchange.sendResponseHeaders(200, 0);
                int taskId = task.getId();
                System.out.println("Задача успешно добавлена по id " + taskId);
            }
        } catch (JsonSyntaxException ex) {
            httpExchange.sendResponseHeaders(400,0);
            System.out.println("Введен неправильный формат запроса");
        }
    }

    private void deleteTask(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            taskManager.deleteAllTasks();
            httpExchange.sendResponseHeaders(200, 0);
            System.out.println("Удаление всех задач прошло успешно");
        } else {
            String pathId = query.replaceFirst("id=", "");
            int id = parsePathId(pathId);
            if (id != -1) {
                taskManager.deleteTask(id);
                System.out.println("Задача с id " + id + " успешно удалена!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("Введен некорректный id" + pathId);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
    }
}
