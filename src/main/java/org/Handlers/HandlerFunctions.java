package org.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.Exceptions.NotAllowedHttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HandlerFunctions {
    public static Map<String, Object> getDataFromPost(HttpExchange exchange) throws IOException, NotAllowedHttpMethod {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            throw new NotAllowedHttpMethod();
        }

        String body;
        try (InputStream is = exchange.getRequestBody()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        Gson gson = new Gson();
        return gson.fromJson(body, Map.class);
    }

    public static void sendStringResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        sendResponse(exchange, response, code);
    }

    public static void sendJsonResponse(HttpExchange exchange, String json, int code) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        sendResponse(exchange, json, code);
    }

    private static void sendResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.sendResponseHeaders(code, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public static void sendUnknownExceptionResponse(HttpExchange exchange, Exception e) throws IOException {
        String errorResp = "Произошла неизвестная ошибка: " + e.getClass() + "\n" + e.getMessage() + "\nСообщите об этой ошибке в поддержку";
        sendStringResponse(exchange, errorResp, 502);
    }
}
