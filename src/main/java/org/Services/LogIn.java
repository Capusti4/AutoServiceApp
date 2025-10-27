package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

import static org.Services.ServiceFunctions.GenerateSessionToken;
import static org.Services.ServiceFunctions.GetCollection;

public class LogIn {
    public static String[] logIn(String username, String password, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = GetCollection(mongoClient, requestURI);
        String token = GenerateSessionToken();
        UpdateResult result = usersCollection.updateOne(
                new Document("username", username).append("password", password),
                Updates.push("sessionTokens", token)
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
