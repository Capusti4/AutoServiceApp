package com.example.AutoServiceApp.Services;

import com.example.AutoServiceApp.DTO.LoginRequest;
import com.example.AutoServiceApp.DTO.LoginResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.example.AutoServiceApp.Exceptions.IncorrectUsernameOrPassword;
import org.bson.Document;

import static com.example.AutoServiceApp.Services.ServiceFunctions.generateSessionToken;
import static com.example.AutoServiceApp.Services.ServiceFunctions.getCollection;

public class Login {
    public static LoginResponse login(LoginRequest request, String userType) {
        MongoCollection<Document> usersCollection = getCollection(userType);
        String username = request.username();
        String password = request.password();
        Document user = usersCollection.find(new Document("username", username).append("password", password)).first();
        if (user == null) {
            throw new IncorrectUsernameOrPassword();
        }
        String token = generateSessionToken();
        usersCollection.updateOne(
                new Document("username", username).append("password", password),
                Updates.push("sessionTokens", token)
        );

        UserDTO userDTO = new UserDTO(
                (String) user.get("username"),
                (String) user.get("firstName"),
                (String) user.get("lastName"),
                (String) user.get("phoneNum")
        );
        SessionDTO sessionDTO = new SessionDTO(
                (String) user.get("username"),
                token
        );

        return new LoginResponse(
                "Успешный вход",
                userDTO,
                sessionDTO
        );
    }
}
