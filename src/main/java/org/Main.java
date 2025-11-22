package org;

import com.sun.net.httpserver.HttpServer;
import org.Handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        CreateClientContext(server);
        CreateWorkerContext(server);
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на http://localhost:8080/");
    }

    public static void CreateClientContext(HttpServer server) {
        server.createContext("/client/register", new RegisterHandler());
        server.createContext("/client/logIn", new LogInHandler());
        server.createContext("/client/checkSession", new CheckSessionHandler());
        server.createContext("/client/deleteSession", new DeleteSessionHandler());
        server.createContext("/client/createOrder", new CreateOrderHandler());
        server.createContext("/client/getCompletedOrders", new GetCompletedOrdersHandler());
        server.createContext("/client/getNotifications", new GetNotificationsHandler());
        server.createContext("/client/getNotificationsAmount", new GetNotificationsAmountHandler());
        server.createContext("/client/readNotification", new ReadNotificationHandler());
        server.createContext("/client/sendFeedback", new SendFeedbackHandler());
        server.createContext("/client/getFeedbacks", new GetFeedbacksHandler());
    }

    public static void CreateWorkerContext(HttpServer server) {
        server.createContext("/worker/register", new RegisterHandler());
        server.createContext("/worker/logIn", new LogInHandler());
        server.createContext("/worker/checkSession", new CheckSessionHandler());
        server.createContext("/worker/deleteSession", new DeleteSessionHandler());
        server.createContext("/worker/getNewOrders", new GetNewOrdersHandler());
        server.createContext("/worker/getActiveOrders", new GetActiveOrdersHandler());
        server.createContext("/worker/getCompletedOrders", new GetCompletedOrdersHandler());
        server.createContext("/worker/startOrder", new StartOrderHandler());
        server.createContext("/worker/completeOrder", new CompleteOrderHandler());
        server.createContext("/worker/getNotifications", new GetNotificationsHandler());
        server.createContext("/worker/getNotificationsAmount", new GetNotificationsAmountHandler());
        server.createContext("/worker/readNotification", new ReadNotificationHandler());
        server.createContext("/worker/sendFeedback", new SendFeedbackHandler());
        server.createContext("/worker/getFeedbacks", new GetFeedbacksHandler());
    }
}
