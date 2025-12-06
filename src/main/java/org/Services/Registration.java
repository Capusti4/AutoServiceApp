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
    public static String[] Register(User user, String requestURI) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoCollection<Document> usersCollection = GetCollection(mongoClient, requestURI);

        String username = user.username();
        String phoneNum = user.phoneNum();
        CheckUsername(usersCollection, username);
        CheckPhoneNumber(usersCollection, phoneNum);

        String token = GenerateSessionToken();

        Document clientDoc = CreateClientDoc(user, token);
        usersCollection.insertOne(clientDoc);
        mongoClient.close();
        clientDoc.remove("password");
        clientDoc.remove("_id");
        clientDoc.remove("sessionTokens");
        return new String[]{clientDoc.toJson(), token};
    }

    static void CheckUsername(MongoCollection<Document> usersCollection, String username) {
        Document found = usersCollection.find(new Document("username", username)).first();
        if (found != null) {
            throw new UsernameAlreadyExists();
        }
        if (!username.matches("[A-Za-z0-9]*") || Character.isDigit(username.charAt(0))) {
            throw new IncorrectUsername();
        }
    }

    static void CheckPhoneNumber(MongoCollection<Document> usersCollection, String phoneNum) {
        Document found = usersCollection.find(new Document("phoneNum", phoneNum)).first();
        if (found != null) {
            throw new PhoneNumberAlreadyExists();
        }
    }

    static Document CreateClientDoc(User user, String token) {
        return new Document()
                .append("username", user.username())
                .append("firstName", user.firstName())
                .append("lastName", user.lastName())
                .append("phoneNum", user.phoneNum())
                .append("password", user.password())
                .append("sessionTokens", Collections.singletonList(token));
    }
}
