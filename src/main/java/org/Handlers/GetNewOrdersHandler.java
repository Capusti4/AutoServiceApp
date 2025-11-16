package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.NewOrdersGiver.GetNewOrdersList;
import static org.Services.TokenChecker.GetUserData;

public class GetNewOrdersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = (String) data.get("username");
            String sessionToken = (String) data.get("sessionToken");
            if (GetUserData(username, sessionToken, "/worker/") == null) {
                SendStringResponse(exchange, "Токен сессии истек", 409);
                return;
            }

            String[] orders = Objects.requireNonNull(GetNewOrdersList(username)).toArray(new String[0]);
            SendJsonResponse(exchange, "{\"orders\": " + Arrays.toString(orders) + "}", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
