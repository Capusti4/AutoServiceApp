package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.Exceptions.NotAllowedHttpMethod;

import java.io.IOException;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.LogIn.logIn;

public class LogInHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String response = CreateResponse(exchange);
            SendJsonResponse(exchange, response, 200);
        } catch (NotAllowedHttpMethod | IncorrectUsernameOrPassword e) {
            SendStringResponse(exchange, e.getMessage(), 409);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static String CreateResponse(HttpExchange exchange) throws Exception {
        Map<String, Object> data = GetDataFromPost(exchange);
        String username = data.get("username").toString();
        String password = data.get("password").toString();
        String[] userInfoAndToken = logIn(username, password, String.valueOf(exchange.getRequestURI()));
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
