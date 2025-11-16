package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.ActiveOrdersGiver.GetActiveOrders;
import static org.Services.TokenChecker.GetUserData;

public class GetActiveOrdersHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            String username = (String) data.get("username");
            String sessionToken = (String) data.get("sessionToken");
            Document workerData = (Document) Objects.requireNonNull(GetUserData(username, sessionToken, "/worker/")).get("userData");

            if (workerData == null) {
                SendStringResponse(exchange, "Токен сессии истек", 409);
                return;
            }

            ObjectId workerId = (ObjectId) workerData.get("_id");
            ArrayList<String> activeOrders = GetActiveOrders(workerId);
            SendJsonResponse(exchange, activeOrders.toString(), 200);
        } catch (Exception e){
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
