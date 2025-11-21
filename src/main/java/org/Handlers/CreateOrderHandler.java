package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Order;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.OrderCreator.CreateOrder;
import static org.Services.UserIdGiver.GetUserId;

public class CreateOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            createOrder(exchange);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static void createOrder(HttpExchange exchange) throws Exception {
        Map<String, Object> data = GetDataFromPost(exchange);
        if (data == null) {
            return;
        }
        int orderTypeId = Integer.parseInt((String) data.get("orderTypeId"));
        String comment = (String) data.get("comment");
        ObjectId userId = GetUserId((Map<String, Object>) data.get("userSessionInfo"), exchange.getRequestURI().toString());
        Order order;
        if (orderTypeId == 0) {
            order = new Order((String) data.get("orderType"), userId, comment);
        } else {
            order = new Order(orderTypeId, userId, comment);
        }
        CreateOrder(order);
        SendStringResponse(exchange, "Заказ успешно создан", 201);
    }
}
