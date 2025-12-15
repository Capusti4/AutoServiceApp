package com.example.AutoServiceApp.Objects;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBCollection {
    private static MongoClient mongoClient;

    public static MongoClient getClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
        }
        return mongoClient;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
