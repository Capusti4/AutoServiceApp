package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.NotificationReader;
import org.Services.UserIdGiver;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class ReadAllNotificationsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            ObjectId userId = UserIdGiver.getUserId(data, exchange.getRequestURI().toString());
            NotificationReader.readAllNotifications(userId);
            sendStringResponse(exchange, "Все уведомления успешно прочитаны", 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
