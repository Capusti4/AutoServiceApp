package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.Exceptions.NotAllowedHttpMethod;
import org.Services.LogIn;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class LogInHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String response = createResponse(exchange);
            sendJsonResponse(exchange, response, 200);
        } catch (NotAllowedHttpMethod | IncorrectUsernameOrPassword e) {
            sendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            sendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static String createResponse(HttpExchange exchange) throws Exception {
        Map<String, Object> data = getDataFromPost(exchange);
        String username = data.get("username").toString();
        String password = data.get("password").toString();
        String[] userInfoAndToken = LogIn.logIn(username, password, String.valueOf(exchange.getRequestURI()));
        String userInfo = userInfoAndToken[0];
        String token = userInfoAndToken[1];

        return String.format("""
                    {
                        "answer": "Юзер успешно вошел",
                        "userData": %s,
                        "sessionInfo": { "username": "%s", "sessionToken": "%s" }
                    }
                    """, userInfo, username, token);
    }

}
