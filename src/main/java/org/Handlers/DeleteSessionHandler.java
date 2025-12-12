package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.TokenDeleter;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class DeleteSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = getDataFromPost(exchange);
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            TokenDeleter.deleteSessionToken(username, sessionToken, String.valueOf(exchange.getRequestURI()));
            sendStringResponse(exchange, "Сессия успешно удалена", 200);
        } catch (NotAllowedHttpMethod e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
