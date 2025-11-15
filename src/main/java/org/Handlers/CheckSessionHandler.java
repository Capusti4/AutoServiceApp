package org.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.Handlers.HandlerFunctions.*;
import static org.Services.TokenChecker.CheckToken;

public class CheckSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        Map<String, Object> data = GetDataFromPost(exchange);
        if (data == null) { return; }

        String username = data.get("username").toString();
        String sessionToken = data.get("sessionToken").toString();
        try{
            String userInfo = Objects.requireNonNull(CheckToken(username, sessionToken, String.valueOf(exchange.getRequestURI()))).toJson();
            if (userInfo != null){
                SendJsonResponse(exchange, userInfo, 200);
            }else{
                SendStringResponse(exchange, "Срок действия сессии истек", 409);
            }
        } catch (Exception e){
            UnknownException(exchange, e);
        }
    }
}
