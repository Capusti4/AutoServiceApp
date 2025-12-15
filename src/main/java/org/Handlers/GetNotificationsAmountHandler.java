package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.AppException;
import org.Services.NotificationsAmountGiver;
import org.Services.UserIdGiver;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class GetNotificationsAmountHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            ObjectId userId = UserIdGiver.getUserId(data, exchange.getRequestURI().toString());
            int amount = NotificationsAmountGiver.getNotificationsAmount(userId);
            sendJsonResponse(exchange, amount + "", 200);
        } catch (AppException e) {
            sendStringResponse(exchange, e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
