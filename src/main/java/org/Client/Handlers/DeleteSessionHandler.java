package org.Client.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

import static org.Client.Handlers.HandlerFunctions.*;
import static org.Client.Services.DeleteToken.DeleteSessionToken;

public class DeleteSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            DeleteSessionToken(username, sessionToken);
            SendStringResponse(exchange, "Сессия успешно удалена", 200);
        } catch (Exception e){
            UnknownException(exchange, e);
        }
    }
}
