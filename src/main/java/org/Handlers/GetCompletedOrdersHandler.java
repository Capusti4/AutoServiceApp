package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.CompletedOrdersGiver;
import org.Services.TokenChecker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class GetCompletedOrdersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            TokenChecker.checkUserToken(data, exchange.getRequestURI().toString());
            String[] completedOrders = CompletedOrdersGiver.getCompletedOrders();
            sendJsonResponse(exchange, Arrays.toString(completedOrders), 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
