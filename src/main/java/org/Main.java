package org;

import com.sun.net.httpserver.HttpServer;
import org.Client.Handlers.CheckSessionHandler;
import org.Client.Handlers.DeleteSessionHandler;
import org.Client.Handlers.LogInHandler;
import org.Client.Handlers.RegisterHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/client/register", new RegisterHandler());
        server.createContext("/client/logIn", new LogInHandler());
        server.createContext("/client/checkSession", new CheckSessionHandler());
        server.createContext("/client/deleteSession", new DeleteSessionHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на http://localhost:8080/");
    }
}
