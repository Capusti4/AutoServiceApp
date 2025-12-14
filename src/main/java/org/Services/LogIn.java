package org.Services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

import static org.Services.ServiceFunctions.generateSessionToken;
import static org.Services.ServiceFunctions.getCollection;

public class LogIn {
    public static String[] logIn(String username, String password, String requestURI) throws Exception {
        MongoCollection<Document> usersCollection = getCollection(requestURI);
        Document found = usersCollection.find(new Document("username", username).append("password", password)).first();
        if (found == null) {
            throw new IncorrectUsernameOrPassword();
        }
        String token = generateSessionToken();
        usersCollection.updateOne(
                new Document("username", username).append("password", password),
                Updates.push("sessionTokens", token)
        );

        found.remove("password");
        found.remove("_id");
        found.remove("sessionTokens");
        return new String[]{found.toJson(), token};
    }
}
