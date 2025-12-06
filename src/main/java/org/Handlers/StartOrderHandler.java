package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectOrderId;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.OrderStarter.StartOrder;
import static org.Services.UserIdGiver.GetUserId;

public class StartOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            var workerId = GetUserId(data, exchange.getRequestURI().toString());
            ObjectId orderId = new ObjectId((String) data.get("orderId"));
            StartOrder(orderId, workerId);
            SendStringResponse(exchange, "Заказ успешно взят в работу", 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (IncorrectOrderId e) {
            SendStringResponse(exchange, e.getMessage(), 400);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        }
    }
}
