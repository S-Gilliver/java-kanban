package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiToken;
    private final String url;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = registration(uri);
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Не удалось обработать запрос");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        apiToken = response.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Не удалось сохранить данные");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.statusCode());
    }

    public String load(String key) throws RuntimeException {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = registration(uri);

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, handler);
            if (response.statusCode() != 200) {
                System.out.println("Во время запроса произошла ошибка");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);

        }
        return response.body();
    }

    private HttpRequest registration(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        return request;
    }
}
