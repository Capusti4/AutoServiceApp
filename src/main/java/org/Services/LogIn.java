package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

import static org.Services.ServiceFunctions.generateSessionToken;
import static org.Services.ServiceFunctions.getCollection;

public class LogIn {
    public static String[] logIn(String username, String password, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = getCollection(mongoClient, requestURI);
        Document found = usersCollection.find(new Document("username", username).append("password", password)).first();
        if (found == null) {
            throw new IncorrectUsernameOrPassword();
        }
        String token = generateSessionToken();
        usersCollection.updateOne(
                new Document("username", username).append("password", password),
                Updates.push("sessionTokens", token)
        );
        mongoClient.close();

        found.remove("password");
        found.remove("_id");
        found.remove("sessionTokens");
        return new String[]{found.toJson(), token};
    }
}
