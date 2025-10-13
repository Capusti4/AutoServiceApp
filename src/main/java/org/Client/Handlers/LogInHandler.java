package org.Client.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.Exceptions.IncorrectUsernameOrPassword;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.Client.Handlers.HandlerFunctions.*;
import static org.Client.Services.LogIn.logIn;

public class LogInHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);

            assert data != null;
            String username = data.get("username").toString();
            String password = data.get("password").toString();
            String[] userInfoAndToken = logIn(username, password);
            String userInfo = userInfoAndToken[0];
            String token = userInfoAndToken[1];

            String response = String.format("""
                    {
                        "answer": "Юзер успешно вошел",
                        "userData": %s,
                        "sessionToken": "%s"
                    }
                    """, userInfo, token);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(201, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IncorrectUsernameOrPassword e){
            SendError(exchange, e.getMessage(), 409);
        }
        catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
