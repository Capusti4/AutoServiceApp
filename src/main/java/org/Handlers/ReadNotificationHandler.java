package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NotificationReader.ReadNotification;
import static org.Services.TokenChecker.CheckUserToken;

public class ReadNotificationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            CheckUserToken(data, exchange.getRequestURI().toString());
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            ReadNotification(notificationId);
            SendStringResponse(exchange, "Сообщение прочитано", 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
