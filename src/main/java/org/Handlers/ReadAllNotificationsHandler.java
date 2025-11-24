package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationReader.ReadAllNotifications;
import static org.Services.UserIdGiver.GetUserId;

public class ReadAllNotificationsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            ObjectId userId = GetUserId(data, exchange.getRequestURI().toString());
            if (UserIdIsNotCorrect(userId, exchange)) { return; }
            ReadAllNotifications(userId);
            SendStringResponse(exchange, "Все уведомления успешно прочитаны", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
