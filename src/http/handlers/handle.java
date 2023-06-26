package http.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

abstract public class handle {

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }
}
