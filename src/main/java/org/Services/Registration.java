package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.Exceptions.IncorrectUsername;
import org.Exceptions.PhoneNumberAlreadyExists;
import org.Exceptions.UsernameAlreadyExists;
import org.User;
import org.bson.Document;

import java.util.Collections;

import static org.Services.ServiceFunctions.GenerateSessionToken;
import static org.Services.ServiceFunctions.GetCollection;

public class Registration {
    public static String[] register(User user, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = GetCollection(mongoClient, requestURI);
        String username = user.getUsername();
        Document found = usersCollection.find(new Document("username", username)).first();
        if (found != null) {
            throw new UsernameAlreadyExists();
        }
        found = usersCollection.find(new Document("phoneNum", user.getPhoneNum())).first();
        if (found != null) {
            throw new PhoneNumberAlreadyExists();
        }

        if (!username.matches("[A-Za-z0-9]*")){
            throw new IncorrectUsername();
        }
        if (Character.isDigit(username.charAt(0))){
            throw new IncorrectUsername();
        }

        String token = GenerateSessionToken();

        Document clientDoc = new Document()
                .append("username", username)
                .append("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("phoneNum", user.getPhoneNum())
                .append("password", user.getPassword())
                .append("sessionTokens", Collections.singletonList(token));
        usersCollection.insertOne(clientDoc);
        mongoClient.close();
        clientDoc.remove("password");
        clientDoc.remove("_id");
        clientDoc.remove("sessionTokens");
        return new String[] {clientDoc.toJson(), token};
    }
}
