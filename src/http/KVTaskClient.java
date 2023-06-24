package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String API_TOKEN;
    private final String URL;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.URL = url;
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);
        API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(URL + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);
        System.out.println(response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(URL + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);
        return response.body();
    }
}
