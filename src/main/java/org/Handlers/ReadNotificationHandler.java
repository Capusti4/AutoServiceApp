package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.NotificationReader;
import org.Services.TokenChecker;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class ReadNotificationHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            TokenChecker.checkUserToken(data, exchange.getRequestURI().toString());
            ObjectId notificationId = new ObjectId((String) data.get("notificationId"));
            NotificationReader.readNotification(notificationId);
            sendStringResponse(exchange, "Сообщение прочитано", 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
