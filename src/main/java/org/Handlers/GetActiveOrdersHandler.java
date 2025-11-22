package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.ActiveOrdersGiver.GetActiveOrders;
import static org.Services.UserIdGiver.GetUserId;

public class GetActiveOrdersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            ObjectId workerId = GetUserId(data, exchange.getRequestURI().toString());
            if (UserIdIsNotCorrect(workerId, exchange)) { return; }
            ArrayList<String> activeOrders = GetActiveOrders(workerId);
            SendJsonResponse(exchange, activeOrders.toString(), 200);
        } catch (Exception e){
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
