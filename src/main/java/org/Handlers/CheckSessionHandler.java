package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.AppException;
import org.Services.TokenChecker;
import org.bson.Document;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class CheckSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            Document user = TokenChecker.getUserData(username, sessionToken, String.valueOf(exchange.getRequestURI()));
            sendJsonResponse(exchange, user.toJson(), 200);
        } catch (AppException e) {
            sendStringResponse(exchange, e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
