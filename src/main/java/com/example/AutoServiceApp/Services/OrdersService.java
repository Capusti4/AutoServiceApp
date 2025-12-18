package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.GetOrderResponse;
import com.example.AutoServiceApp.DTO.OrderDTO;
import com.example.AutoServiceApp.Exceptions.IncorrectOrderId;
import com.example.AutoServiceApp.Exceptions.IncorrectSessionToken;
import com.example.AutoServiceApp.Objects.MongoDBCollection;
import com.example.AutoServiceApp.Objects.Order;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;


public class OrdersService {
    public static void createOrder(Order order) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> collection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderDoc = new Document()
                .append("customerId", order.getCustomerId())
                .append("typeID", order.getTypeID())
                .append("type", order.getType())
                .append("comment", order.getComment())
                .append("hasCustomerFeedback", false)
                .append("hasWorkerFeedback", false);
        collection.insertOne(orderDoc);
    }

    public static void completeOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        Document found = activeOrdersCollection.find(new Document("_id", orderId).append("workerId", workerId)).first();
        if (found == null) {
            throw new IncorrectOrderId();
        }
        activeOrdersCollection.deleteOne(found);
        completedOrdersCollection.insertOne(found
                .append("hasCustomerFeedback", false)
                .append("hasWorkerFeedback", false));
        sendNotifications(found);
    }

    static void sendNotifications(Document found) {
        ObjectId workerId = (ObjectId) found.get("workerId");
        ObjectId customerId = (ObjectId) found.get("customerId");
        NotificationsService.createNotification(
                customerId,
                3,
                "Ваш заказ \"" + found.get("type") + "\" с комментарием \"" +
                found.get("comment") + "\" успешно завершен!"
        );
        NotificationsService.createNotification(
                workerId, 5, "Вы завершили заказ " + found.get("_id").toString()
        );
    }

    public static GetOrderResponse getNewOrdersList() throws IncorrectSessionToken {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        List<OrderDTO> newOrders = getOrders(newOrdersCollection);
        return new GetOrderResponse(newOrders);
    }

    public static GetOrderResponse getActiveOrders(ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        List<OrderDTO> activeOrders = getOrders(activeOrdersCollection);
        activeOrders.removeIf(activeOrder -> !activeOrder.workerId().equals(workerId));
        return new GetOrderResponse(activeOrders);
    }

    public static GetOrderResponse getCompletedOrders(ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> completedOrdersCollection = mongoClient.getDatabase("Orders").getCollection("CompletedOrders");
        List<OrderDTO> completedOrders = getOrders(completedOrdersCollection);
        completedOrders.removeIf(completedOrder -> !completedOrder.workerId().equals(workerId));
        return new GetOrderResponse(completedOrders);
    }

    static List<OrderDTO> getOrders(MongoCollection<Document> ordersCollection) {
        List<OrderDTO> orders = new ArrayList<>();
        List<ObjectId> toDelete = new ArrayList<>();
        List<Document> allOrders = ordersCollection.find().into(new ArrayList<>());
        MongoClient mongoClient = MongoDBCollection.getClient();

        MongoCollection<Document> clientsCollection = mongoClient.getDatabase("Users").getCollection("Clients");
        Set<Object> customersId = allOrders.stream()
                .map(order -> order.get("customerId"))
                .collect(Collectors.toSet());
        Map<Object, Document> customersMap = new HashMap<>();
        clientsCollection.find(new Document("_id", new Document("$in", customersId)))
                .forEach(customer -> customersMap.put(customer.get("_id"), customer));

        for (Document order : ordersCollection.find()) {
            Document customer = customersMap.get(order.get("customerId"));
            if (customer == null) {
                toDelete.add(new ObjectId(order.get("_id").toString()));
            } else {
                order.append("customerFirstName", customer.get("firstName"));
                order.append("customerLastName", customer.get("lastName"));
                order.append("customerPhoneNum", customer.get("phoneNumber"));
                orders.add(new OrderDTO(
                        (ObjectId) order.get("_id"),
                        (ObjectId) order.get("customerId"),
                        (Integer) order.get("typeId"),
                        (String) order.get("type"),
                        (String) order.get("comment"),
                        (ObjectId) order.get("workerId"),
                        (Boolean) order.get("hasCustomerFeedback"),
                        (Boolean) order.get("hasWorkerFeedback")
                ));
            }
        }

        if (!toDelete.isEmpty()) {
            ordersCollection.deleteMany(new Document("_id", new Document("$in", toDelete)));
        }

        return orders;
    }

    public static void startOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> newOrdersCollection = mongoClient.getDatabase("Orders").getCollection("NewOrders");
        Document orderInfo = newOrdersCollection.find(new Document("_id", orderId)).first();
        if (orderInfo == null) {
            throw new IncorrectOrderId();
        }

        newOrdersCollection.deleteOne(orderInfo);
        orderInfo.append("workerId", workerId);
        MongoCollection<Document> activeOrdersCollection = mongoClient.getDatabase("Orders").getCollection("ActiveOrders");
        activeOrdersCollection.insertOne(orderInfo);

        String text = "Ваш заказ \"" + orderInfo.get("type") + "\" с комментарием \"" + orderInfo.get("comment") + "\" успешно взят в работу!";
        NotificationsService.createNotification((ObjectId) orderInfo.get("customerId"), 2, text);
    }
}
