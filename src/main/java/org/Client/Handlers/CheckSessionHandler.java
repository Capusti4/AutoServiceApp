package org.Client.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

import static org.Client.Handlers.HandlerFunctions.*;
import static org.Client.Services.TokenChecker.CheckToken;

public class CheckSessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException{
        Map<String, Object> data = GetDataFromPost(exchange);
        if (data == null) { return; }

        String username = data.get("username").toString();
        String sessionToken = data.get("sessionToken").toString();
        String userInfo = CheckToken(username, sessionToken);
        if (userInfo != null){
            SendJsonResponse(exchange, userInfo, 200);
        }else{
            SendStringResponse(exchange, "Срок действия сессии истек", 409);
        }
    }
}
