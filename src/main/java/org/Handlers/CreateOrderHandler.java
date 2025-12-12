package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Order;
import org.Services.OrderCreator;
import org.Services.UserIdGiver;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class CreateOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            createOrder(exchange);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static void createOrder(HttpExchange exchange) throws Exception {
        Map<String, Object> data = getDataFromPost(exchange);
        int orderTypeId = Math.toIntExact(Math.round((double) data.get("orderTypeId")));
        String comment = (String) data.get("comment");
        Map<String, Object> userInfo = (Map<String, Object>) data.get("userSessionInfo");
        ObjectId userId = UserIdGiver.getUserId(userInfo, exchange.getRequestURI().toString());
        Order order;
        if (orderTypeId == 0) {
            order = new Order((String) data.get("orderType"), userId, comment);
        } else {
            order = new Order(orderTypeId, userId, comment);
        }
        OrderCreator.createOrder(order);
        sendStringResponse(exchange, "Заказ успешно создан", 201);
    }
}
