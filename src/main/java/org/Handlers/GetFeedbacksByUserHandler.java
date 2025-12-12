package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.FeedbacksGiver;
import org.Services.UserIdGiver;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class GetFeedbacksByUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            ObjectId userId = UserIdGiver.getUserId(data, exchange.getRequestURI().toString());
            String[] feedbacks = FeedbacksGiver.getFeedbacksByUser(userId);
            sendJsonResponse(exchange, Arrays.toString(feedbacks), 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
