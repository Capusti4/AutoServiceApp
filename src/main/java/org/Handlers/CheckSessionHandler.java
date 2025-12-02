package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.TokenChecker.GetUserData;

public class CheckSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            Document user = GetUserData(username, sessionToken, String.valueOf(exchange.getRequestURI()));
            SendResponse(exchange, user);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static void SendResponse(HttpExchange exchange, Document user) throws Exception {
        if (user != null) {
            SendJsonResponse(exchange, user.toJson(), 200);
        } else {
            SendStringResponse(exchange, "Срок действия сессии истек", 409);
        }
    }
}
