package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationsGiver.GetNotifications;
import static org.Services.UserIdGiver.GetUserId;

public class GetNotificationsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            var userId = GetUserId(data);
            ArrayList<String> notifications = GetNotifications(userId);
            SendJsonResponse(exchange, notifications.toString(), 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
