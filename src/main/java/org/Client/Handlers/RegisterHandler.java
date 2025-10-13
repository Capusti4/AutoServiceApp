package org.Client.Handlers;

import com.sun.net.httpserver.*;
import org.Exceptions.*;
import org.Client.Services.Registration;

import java.io.*;
import java.util.Map;

import static org.Client.Handlers.HandlerFunctions.*;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Map<String, Object> data = GetDataFromPost(exchange);

            assert data != null;
            String username = (String) data.get("username");
            String password = (String) data.get("password");
            String firstName = (String) data.get("firstName");
            String lastName = (String) data.get("lastName");
            String phoneNum = (String) data.get("phoneNum");
            String[] userInfoAndToken = Registration.register(username, password, firstName, lastName, phoneNum);
            String userInfo = userInfoAndToken[0];
            String token = userInfoAndToken[1];
            String response = String.format("""
                    {
                        "answer": "Юзер добавлен успешно",
                        "userInfo": %s,
                        "sessionToken": "%s"
                    }
                    """, userInfo, token);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(201, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (IncorrectName | IncorrectPhoneNumber | UsernameAlreadyExists | IncorrectUsername |
                 PhoneNumberAlreadyExists e) {
            String errorResp = e.getMessage();
            int errorCode = 500;
            SendError(exchange, errorResp, errorCode);
        } catch (Exception e) {
            UnknownException(exchange, e);
        } finally {
            exchange.close();
        }
    }
}
