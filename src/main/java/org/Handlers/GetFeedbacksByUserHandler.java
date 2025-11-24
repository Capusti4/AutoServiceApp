package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Handlers.HandlerFunctions.UnknownException;
import static org.Services.FeedbacksGiver.GetFeedbacksByUser;
import static org.Services.UserIdGiver.GetUserId;

public class GetFeedbacksByUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            ObjectId userId = GetUserId(data, exchange.getRequestURI().toString());
            if (UserIdIsNotCorrect(userId, exchange)) { return; }
            String[] feedbacks = GetFeedbacksByUser(userId);
            SendJsonResponse(exchange, "{\"feedbacks\": " + Arrays.toString(feedbacks) + "}", 200);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
