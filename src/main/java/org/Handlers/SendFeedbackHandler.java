package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Exceptions.UnknownOrderId;
import org.Services.FeedbackCreator;
import org.Services.TokenChecker;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class SendFeedbackHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            TokenChecker.checkUserToken(data, exchange.getRequestURI().toString());
            ObjectId authorId = new ObjectId((String) data.get("authorId"));
            ObjectId targetId = new ObjectId((String) data.get("targetId"));
            ObjectId orderId = new ObjectId((String) data.get("orderId"));
            int rating = Math.toIntExact(Math.round((double) data.get("rating")));
            String comment = (String) data.get("comment");
            FeedbackCreator.createFeedback(authorId, targetId, orderId, rating, comment);
            sendStringResponse(exchange, "Спасибо за Ваш отзыв!", 201);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (UnknownOrderId e) {
            sendStringResponse(exchange, e.getMessage(), 400);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
