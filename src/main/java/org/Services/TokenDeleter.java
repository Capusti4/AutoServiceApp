package org.Services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import static org.Services.ServiceFunctions.getCollection;

public class TokenDeleter {
    public static void deleteSessionToken(String username, String token, String requestURI) throws Exception {
        MongoCollection<Document> usersCollection = getCollection(requestURI);

        Bson filter = Filters.eq("username", username);
        Bson update = Updates.pull("sessionTokens", token);

        usersCollection.updateOne(filter, update);
    }
}
