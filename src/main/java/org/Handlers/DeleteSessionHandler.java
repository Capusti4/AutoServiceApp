package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.NotAllowedHttpMethod;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.DeleteToken.DeleteSessionToken;

public class DeleteSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);
            String username = data.get("username").toString();
            String sessionToken = data.get("sessionToken").toString();
            DeleteSessionToken(username, sessionToken, String.valueOf(exchange.getRequestURI()));
            SendStringResponse(exchange, "Сессия успешно удалена", 200);
        } catch (NotAllowedHttpMethod e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
