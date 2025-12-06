package org.Handlers;

import com.sun.net.httpserver.*;
import org.Exceptions.*;
import org.Services.Registration;
import org.User;

import java.io.*;
import java.util.Map;

import static org.Handlers.HandlerFunctions.*;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            User user = CreateUser(exchange);
            String response = CreateResponse(user, exchange);
            SendJsonResponse(exchange, response, 201);
        } catch (IncorrectName | IncorrectPhoneNumber | UsernameAlreadyExists | IncorrectUsername |
                 PhoneNumberAlreadyExists | NotAllowedHttpMethod e) {
            String errorResp = e.getMessage();
            int errorCode = 500;
            SendStringResponse(exchange, errorResp, errorCode);
        } catch (Exception e) {
            SendUnknownExceptionResponse(exchange, e);
        } finally {
            exchange.close();
        }
    }

    static User CreateUser(HttpExchange exchange) throws Exception {
        Map<String, Object> data = GetDataFromPost(exchange);
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        String firstName = (String) data.get("firstName");
        String lastName = (String) data.get("lastName");
        String phoneNum = (String) data.get("phoneNum");
        return new User(username, password, firstName, lastName, phoneNum);
    }

    static String CreateResponse(User user, HttpExchange exchange) throws Exception {
        String[] userInfoAndToken = Registration.Register(user, String.valueOf(exchange.getRequestURI()));
        String userInfo = userInfoAndToken[0];
        String token = userInfoAndToken[1];
        return String.format("""
                {
                    "answer": "Юзер создан успешно",
                    "userData": %s,
                    "sessionInfo": { "username": "%s", "sessionToken": "%s" }
                }
                """, userInfo, user.username(), token);
    }
}
