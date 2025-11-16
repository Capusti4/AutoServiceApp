package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectOrderId;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.OrderStarter.StartOrder;
import static org.Services.TokenChecker.GetUserData;

public class StartOrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            Document workerInfo = GetWorkerInfo(data);
            if (workerInfo == null) {
                SendStringResponse(exchange,"Токен сессии недействителен", 400);
                return;
            }
            String stringOrderId = data.get("orderId").toString();
            String stringWorkerId = ((Document) workerInfo.get("userData")).get("_id").toString();
            ObjectId orderId = new ObjectId(stringOrderId.substring(6, stringOrderId.length()-1));
            ObjectId workerId = new ObjectId(stringWorkerId);
            StartOrder(orderId, workerId);
            SendStringResponse(exchange, "Заказ успешно взят в работу", 200);
        }
        catch (IncorrectOrderId e){
            SendStringResponse(exchange, e.getMessage(), 400);
        }
        catch (Exception e) {
            UnknownException(exchange, e);
        }
    }

    static Document GetWorkerInfo(Map<String, Object> data) throws Exception {
        String username = (String) data.get("username");
        String sessionToken = (String) data.get("sessionToken");
        return GetUserData(username, sessionToken, "/worker/");
    }
}
