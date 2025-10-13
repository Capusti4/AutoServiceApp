package org.Client.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

public class LogIn {
    public static String logIn(String username, String password) throws IncorrectUsernameOrPassword {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase usersDatabase = mongoClient.getDatabase("Clients");
        MongoCollection<Document> usersCollection = usersDatabase.getCollection("Clients");

        Document found = usersCollection.find(new Document("username", username).append("password", password)).first();
        if (found == null){
            throw new IncorrectUsernameOrPassword();
        }

        mongoClient.close();
        found.remove("password");
        return found.toJson();
    }
}
