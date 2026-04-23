package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.LoginRequest;
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

    public UserDTO login(LoginRequest request) {
        String username = request.username();
        String password = request.password();

        UserEntity user = userRepository.findByUsername(username);
        if (user == null || !user.checkPassword(password)) {
            throw new IncorrectUsernameOrPassword();
        }
        userRepository.save(user);

        return new UserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isWorker()
        );
    }
}
