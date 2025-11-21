package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.FeedbackCreator.CreateFeedback;
import static org.Services.TokenChecker.GetUserData;

public class SendFeedbackHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            if (data == null) { return; }
            if (!CheckUserData(data, exchange)) { return; }
            ObjectId authorId = new ObjectId((String) data.get("authorId"));
            ObjectId targetId = new ObjectId((String) data.get("targetId"));
            int rating  = Math.toIntExact(Math.round((double) data.get("rating")));
            String comment = (String) data.get("comment");
            CreateFeedback(authorId, targetId, rating, comment);
            SendStringResponse(exchange, "Спасибо за Ваш отзыв!", 201);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }

    public boolean CheckUserData(Map<String, Object> data, HttpExchange exchange) throws Exception {
        String username = (String) data.get("username");
        String sessionToken = (String) data.get("sessionToken");
        Document userData = GetUserData(username, sessionToken, exchange.getRequestURI().toString());
        if (userData == null) {
            SendStringResponse(exchange, "Токен сесии истек", 409);
            return false;
        }
        return true;
    }
}
