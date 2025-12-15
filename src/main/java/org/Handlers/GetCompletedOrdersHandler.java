package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.AppException;
import org.Services.CompletedOrdersGiver;
import org.Services.TokenChecker;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class GetCompletedOrdersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            TokenChecker.checkUserToken(data, exchange.getRequestURI().toString());
            String completedOrdersJson = CompletedOrdersGiver.getCompletedOrders();
            sendJsonResponse(exchange, completedOrdersJson, 200);
        } catch (AppException e) {
            sendStringResponse(exchange, e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
