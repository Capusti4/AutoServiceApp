package org.Client.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HandlerFunctions {
    public static void SendError(HttpExchange exchange, String errorResp, int errorCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(errorCode, errorResp.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorResp.getBytes());
        }
    }

    public static Map<String, Object> GetDataFromPost(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String resp = "Method Not Allowed";
            exchange.sendResponseHeaders(405, resp.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp.getBytes());
            }
            return null;
        }

        String body;
        try (InputStream is = exchange.getRequestBody()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        Gson gson = new Gson();
        return gson.fromJson(body, Map.class);
    }

    public static void UnknownException(HttpExchange exchange, Exception e) throws IOException {
        String errorResp = e.getMessage();
        SendError(exchange, errorResp, 502);
    }

}
