package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.WithUserDataDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectSessionToken;
import com.example.AutoServiceApp.Exception.IncorrectUserType;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUser(WithUserDataDTO request) {
        String username = request.username();
        String sessionToken = request.sessionToken();
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IncorrectSessionToken();
        }
        if (user.checkSessionToken(sessionToken)) {
            return user;
        }
        throw new IncorrectSessionToken();
    }

    public UserEntity getClient(WithUserDataDTO request) {
        UserEntity user = getUser(request);
        if (user.isWorker()) {
            throw new IncorrectUserType();
        }
        return user;
    }
}
