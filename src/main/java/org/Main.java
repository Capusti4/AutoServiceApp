package org;

import com.sun.net.httpserver.HttpServer;
import org.Handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/client/register", new RegisterHandler());
        server.createContext("/client/logIn", new LogInHandler());
        server.createContext("/client/checkSession", new CheckSessionHandler());
        server.createContext("/client/deleteSession", new DeleteSessionHandler());
        server.createContext("/client/createOrder", new CreateOrderHandler());

        server.createContext("/worker/register", new RegisterHandler());
        server.createContext("/worker/logIn", new LogInHandler());
        server.createContext("/worker/checkSession", new CheckSessionHandler());
        server.createContext("/worker/deleteSession", new DeleteSessionHandler());
        server.createContext("/worker/getOrdersList", new GetOrdersListHandler());
        server.createContext("/worker/startOrder", new StartOrderHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на http://localhost:8080/");
    }
}
