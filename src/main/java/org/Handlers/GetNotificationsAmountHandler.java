package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationsAmountGiver.GetNotificationsAmount;
import static org.Services.UserIdGiver.GetUserId;

public class GetNotificationsAmountHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            var userId = GetUserId(data);
            int amount = GetNotificationsAmount(userId);
            SendJsonResponse(exchange, amount + "", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
