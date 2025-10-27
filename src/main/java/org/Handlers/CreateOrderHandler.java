package org.Handlers;

import com.google.gson.internal.LinkedTreeMap;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Order;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.OrderCreator.CreateOrder;

public class CreateOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) {
                return;
            }
            int orderTypeId = Integer.parseInt((String) data.get("orderTypeId"));
            String comment = (String) data.get("comment");
            LinkedTreeMap<String, String> userInfo = (LinkedTreeMap<String, String>) data.get("userInfo");
            Order order;
            if (orderTypeId == 0) {
                order = new Order((String) data.get("orderType"), userInfo, comment);
            } else {
                order = new Order(orderTypeId, userInfo, comment);
            }
            CreateOrder(order);
            SendStringResponse(exchange, "Заказ успешно создан", 201);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
