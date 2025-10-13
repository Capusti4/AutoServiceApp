package org.Client.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

import static org.Client.Services.ServiceFunctions.GenerateSessionToken;

public class LogIn {
    public static String[] logIn(String username, String password) throws IncorrectUsernameOrPassword {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase usersDatabase = mongoClient.getDatabase("Clients");
        MongoCollection<Document> usersCollection = usersDatabase.getCollection("Clients");

        String token = GenerateSessionToken();
        UpdateResult result = usersCollection.updateOne(
                new Document("username", username).append("password", password),
                Updates.push("orders", token)
        );
        if (result.getModifiedCount() == 0) {
            throw new IncorrectUsernameOrPassword();
        }

        Document found = usersCollection.find(new Document("username", username)).first();
        mongoClient.close();

        assert found != null;
        found.remove("password");
        found.remove("_id");
        found.remove("sessionTokens");
        return new String[] {found.toJson(), token};
    }
}
