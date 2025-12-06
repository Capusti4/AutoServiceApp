package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Exceptions.UnknownOrderId;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.FeedbackCreator.CreateFeedback;
import static org.Services.TokenChecker.CheckUserToken;

public class SendFeedbackHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            CheckUserToken(data, exchange.getRequestURI().toString());
            ObjectId authorId = new ObjectId((String) data.get("authorId"));
            ObjectId targetId = new ObjectId((String) data.get("targetId"));
            ObjectId orderId = new ObjectId((String) data.get("orderId"));
            int rating = Math.toIntExact(Math.round((double) data.get("rating")));
            String comment = (String) data.get("comment");
            CreateFeedback(authorId, targetId, orderId, rating, comment);
            SendStringResponse(exchange, "Спасибо за Ваш отзыв!", 201);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (UnknownOrderId e) {
            SendStringResponse(exchange, e.getMessage(), 400);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
