package org.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.SecureRandom;
import java.util.Base64;

public class ServiceFunctions {
    private static final SecureRandom secureRandom = new SecureRandom(); // thread-safe
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static String GenerateSessionToken(){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64UrlEncoder.encodeToString(bytes);
    }

    public static MongoCollection<Document> GetCollection(MongoClient mongoClient, String requestURI) throws Exception {
        MongoDatabase usersDatabase = mongoClient.getDatabase("Users");
        if (requestURI.startsWith("/client/")) {
            return usersDatabase.getCollection("Clients");
        } else if (requestURI.startsWith("/worker/")) {
            return usersDatabase.getCollection("Workers");
        } else {
            throw new Exception("Недопустимый URI");
        }
    }
}
