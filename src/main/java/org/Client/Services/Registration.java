package org.Client.Services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.Exceptions.IncorrectUsername;
import org.Exceptions.PhoneNumberAlreadyExists;
import org.Exceptions.UsernameAlreadyExists;
import org.User;
import org.bson.Document;

public class Registration {
    public static String register(String username, String userFirstName, String userLastName, String userPhoneNum, String userPassword) {
        User user = new User(username, userFirstName, userLastName, userPhoneNum, userPassword);
        return addUser(user);
    }

    static String addUser(User user) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase usersDatabase = mongoClient.getDatabase("Clients");
        MongoCollection<Document> usersCollection = usersDatabase.getCollection("Clients");
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

        Document userDoc = new Document()
                .append("username", username)
                .append("firstName", user.getFirstName())
                .append("secondName", user.getLastName())
                .append("phoneNum", user.getPhoneNum())
                .append("password", user.getPassword());
        usersCollection.insertOne(userDoc);
        mongoClient.close();
        userDoc.remove("password");
        return userDoc.toJson();
    }
}
