package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.GetOrdersResponse;
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
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Users").getCollection("Orders");
        String comment = order.getComment();
        if (order.getComment().isEmpty()) {
            comment = null;
        }
        Document orderDoc = new Document()
                .append("customerId", order.getCustomerId())
                .append("typeId", order.getTypeID())
                .append("type", order.getType())
                .append("comment", comment)
                .append("status", "new")
                .append("hasCustomerFeedback", false)
                .append("hasWorkerFeedback", false);
        ordersCollection.insertOne(orderDoc);
    }

    public static void startOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Users").getCollection("Orders");
        Document order = getOrder(ordersCollection, orderId);
        if (order.get("status").equals("new")) {
            ordersCollection.updateOne(order, new Document("$set", new Document("status", "active")
                    .append("workerId", workerId)));
            String text = "Ваш заказ \"" + order.get("type") + "\" с комментарием \"" + order.get("comment") + "\" успешно взят в работу!";
            NotificationsService.createNotification((ObjectId) order.get("customerId"), 2, text);
        } else {
            throw new IncorrectOrderId();
        }
    }

    public static void completeOrder(ObjectId orderId, ObjectId workerId) {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Users").getCollection("Orders");
        Document order = getOrder(ordersCollection, orderId);
        if (order.get("status").equals("active") && order.get("workerId").equals(workerId)) {
            ordersCollection.updateOne(order, new Document("$set", new Document("status", "completed")));
            NotificationsService.createNotification(
                    (ObjectId) order.get("customerId"),
                    3,
                    "Ваш заказ \"" + order.get("type") + "\" с комментарием \"" +
                    order.get("comment") + "\" успешно завершен!"
            );
            NotificationsService.createNotification(
                    workerId, 5, "Вы завершили заказ " + order.get("_id").toString()
            );
        } else {
            throw new IncorrectOrderId();
        }
    }

    static Document getOrder(MongoCollection<Document> ordersCollection, ObjectId orderId) {
        Document found = ordersCollection.find(new Document("_id", orderId)).first();
        if (found == null) {
            throw new IncorrectOrderId();
        }
        return found;
    }

    public static GetOrdersResponse getUserOrders(ObjectId userId, String userType) throws IncorrectSessionToken {
        List<OrderDTO> allOrders = getOrders();
        List<OrderDTO> orders = new ArrayList<>();
        if (userType.equals("client")) {
            for (OrderDTO order : allOrders) {
                if (order.customerId().equals(userId.toString())) {
                    orders.add(order);
                }
            }
        } else if (userType.equals("worker")) {
            for (OrderDTO order : allOrders) {
                if (order.workerId().equals(userId.toString()) || order.status().equals("new")) {
                    orders.add(order);
                }
            }
        }
        return new GetOrdersResponse(orders);
    }

    static List<OrderDTO> getOrders() {
        MongoClient mongoClient = MongoDBCollection.getClient();
        MongoCollection<Document> ordersCollection = mongoClient.getDatabase("Users").getCollection("Orders");
        List<OrderDTO> orders = new ArrayList<>();
        List<ObjectId> toDelete = new ArrayList<>();
        List<Document> allOrders = ordersCollection.find().into(new ArrayList<>());

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
                String workerId;
                if (order.get("workerId") == null) {
                    workerId = null;
                } else {
                    workerId = order.get("workerId").toString();
                }
                orders.add(new OrderDTO(
                        order.get("_id").toString(),
                        order.get("customerId").toString(),
                        (Integer) order.get("typeId"),
                        (String) order.get("type"),
                        (String) order.get("comment"),
                        workerId,
                        (String) order.get("status"),
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
}
