package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.OrderCompleter.CompleteOrder;
import static org.Services.TokenChecker.GetUserData;

public class CompleteOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = (String) data.get("username");
            String sessionToken = (String) data.get("sessionToken");
            Document workerInfo = GetUserData(username, sessionToken, "/worker/");
            if (workerInfo == null) {
                SendStringResponse(exchange, "Токен сессии истек", 409);
                return;
            }
            ObjectId orderId = new ObjectId((String) data.get("orderId"));
            CompleteOrder(orderId);

            SendStringResponse(exchange, "Заказ успешно завершен", 200);
        } catch (Exception e){
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
