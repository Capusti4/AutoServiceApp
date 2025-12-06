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
    public static Map<String, Object> GetDataFromPost(HttpExchange exchange) throws IOException, NotAllowedHttpMethod {
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

    public static void SendStringResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        SendResponse(exchange, response, code);
    }

    public static void SendJsonResponse(HttpExchange exchange, String json, int code) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        SendResponse(exchange, json, code);
    }

    private static void SendResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.sendResponseHeaders(code, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public static void SendUnknownExceptionResponse(HttpExchange exchange, Exception e) throws IOException {
        String errorResp = "Произошла неизвестная ошибка: " + e.getMessage() + "\nСообщите об этой ошибке в поддержку";
        SendStringResponse(exchange, errorResp, 502);
    }
}
