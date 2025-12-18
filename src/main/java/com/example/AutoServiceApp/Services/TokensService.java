package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.WithUserDataDTO;
import com.example.AutoServiceApp.Exceptions.IncorrectSessionToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.example.AutoServiceApp.Services.ServiceFunctions.getCollection;
import static com.example.AutoServiceApp.Services.ServiceFunctions.getUserDocument;

public class TokensService {
    public static Document getUserData(WithUserDataDTO request, String userType) {
        Document found = getUserDocument(request.username(), userType);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (request.sessionToken().equals(token)) {
                found.remove("sessionTokens");
                found.remove("password");
                return new Document("userData", found).append("sessionToken", token);
            }
        }
        throw new IncorrectSessionToken();
    }

    public static void checkUserToken(WithUserDataDTO request, String userType) {
        String username = request.username();
        String sessionToken = request.sessionToken();
        Document found = getUserDocument(username, userType);
        for (Object token : found.get("sessionTokens", ArrayList.class)) {
            if (sessionToken.equals(token)) {
                return;
            }
        }
        throw new IncorrectSessionToken();
    }

    public static void deleteSessionToken(String username, String token, String userType) {
        MongoCollection<Document> usersCollection = getCollection(userType);

        Bson filter = Filters.eq("username", username);
        Bson update = Updates.pull("sessionTokens", token);

        usersCollection.updateOne(filter, update);
    }
}
