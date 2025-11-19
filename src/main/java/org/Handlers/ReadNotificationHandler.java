package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationReader.ReadNotification;
import static org.Services.TokenChecker.GetUserData;

public class ReadNotificationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = (String) data.get("username");
            String sessionToken = (String) data.get("sessionToken");
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            Document userData = GetUserData(username, sessionToken, exchange.getRequestURI().toString());
            if (userData == null) { return; }
            userData = (Document) userData.get("userData");
            if (userData == null) {
                SendStringResponse(exchange, "Токен сессии истек", 409);
                return;
            }
            ReadNotification(notificationId);
            SendStringResponse(exchange, "Сообщение прочитано", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
