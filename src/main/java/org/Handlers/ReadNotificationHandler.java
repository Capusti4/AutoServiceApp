package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationReader.ReadNotification;
import static org.Services.UserIdGiver.GetUserId;

public class ReadNotificationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            var userId = GetUserId(data, exchange.getRequestURI().toString());
            if (UserIdIsNotCorrect(userId, exchange)) { return; }
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            ReadNotification(notificationId);
            SendStringResponse(exchange, "Сообщение прочитано", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
