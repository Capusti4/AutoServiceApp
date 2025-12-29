package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.LoginRequest;
import com.example.AutoServiceApp.DTO.LoginResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectUsernameOrPassword;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class LoginService {
    public final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request, String userType) {
        String username = request.username();
        String password = request.password();

        UserEntity user = userRepository.findByUsername(username);
        if (user == null || !user.checkPassword(password) || ((user.isWorker() && userType.equals("client")) || (!user.isWorker() && userType.equals("worker")))) {
            throw new IncorrectUsernameOrPassword();
        }
        String sessionToken = user.generateSessionToken();
        userRepository.save(user);
        UserDTO userDTO = new UserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
        SessionDTO sessionDTO = new SessionDTO(
                user.getUsername(),
                sessionToken
        );

        return new LoginResponse(
                "Успешный вход",
                userDTO,
                sessionDTO
        );
    }
}
