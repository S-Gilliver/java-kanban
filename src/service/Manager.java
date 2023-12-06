package service;

import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskManager;
import http.KVServer;
import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Manager {
    private Manager() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultHttpNotLoad() throws IOException {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

    public static TaskManager getDefaultHttpAndLoad(Boolean load) throws IOException {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT, load);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.setPrettyPrinting().create();
    }
}
