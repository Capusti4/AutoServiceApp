package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectSessionToken;
import org.Exceptions.NotAllowedHttpMethod;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.FeedbacksGiver.GetFeedbacksForUser;
import static org.Services.UserIdGiver.GetUserId;

public class GetFeedbacksForUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            ObjectId userId = GetUserId(data, exchange.getRequestURI().toString());
            String[] feedbacks = GetFeedbacksForUser(userId);
            SendJsonResponse(exchange, Arrays.toString(feedbacks), 200);
        } catch (NotAllowedHttpMethod | IncorrectSessionToken e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
