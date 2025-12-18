package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.RegisterResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.mongodb.client.MongoCollection;
import com.example.AutoServiceApp.Exceptions.IncorrectUsername;
import com.example.AutoServiceApp.Exceptions.PhoneNumberAlreadyExists;
import com.example.AutoServiceApp.Exceptions.UsernameAlreadyExists;
import com.example.AutoServiceApp.Objects.User;
import org.bson.Document;

import java.util.Collections;

import static com.example.AutoServiceApp.Services.ServiceFunctions.generateSessionToken;
import static com.example.AutoServiceApp.Services.ServiceFunctions.getCollection;

public class RegistrationService {
    public static RegisterResponse register(User user, String userType) {
        MongoCollection<Document> usersCollection = getCollection(userType);

        String username = user.username();
        String phoneNumber = user.phoneNumber();
        checkUsername(usersCollection, username);
        CheckPhoneNumber(usersCollection, phoneNumber);

        String token = generateSessionToken();

        Document userDoc = CreateUserDoc(user, token);
        usersCollection.insertOne(userDoc);

        UserDTO userDTO = new UserDTO(
                user.username(),
                user.firstName(),
                user.lastName(),
                user.phoneNumber()
        );
        SessionDTO sessionDTO = new SessionDTO(
                user.username(),
                token
        );

        return new RegisterResponse(
                "Аккаунт успешно зарегистрирован",
                userDTO,
                sessionDTO
        );
    }

    static void checkUsername(MongoCollection<Document> usersCollection, String username) {
        Document found = usersCollection.find(new Document("username", username)).first();
        if (found != null) {
            throw new UsernameAlreadyExists();
        }
        if (!username.matches("[A-Za-z0-9]*") || Character.isDigit(username.charAt(0))) {
            throw new IncorrectUsername();
        }
    }

    static void CheckPhoneNumber(MongoCollection<Document> usersCollection, String phoneNumber) {
        Document found = usersCollection.find(new Document("phoneNumber", phoneNumber)).first();
        if (found != null) {
            throw new PhoneNumberAlreadyExists();
        }
    }

    static Document CreateUserDoc(User user, String token) {
        return new Document()
                .append("username", user.username())
                .append("firstName", user.firstName())
                .append("lastName", user.lastName())
                .append("phoneNumber", user.phoneNumber())
                .append("password", user.password())
                .append("sessionTokens", Collections.singletonList(token));
    }
}
