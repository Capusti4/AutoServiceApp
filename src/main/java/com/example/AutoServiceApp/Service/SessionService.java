package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDataDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final UserService userService;

    public SessionService(UserService userService) {
        this.userService = userService;
    }

    public UserDataDTO getUserData(SessionDTO session) {
        UserEntity user = userService.getUser(session);
        return new UserDataDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                session
        );
    }

    public void deleteSessionToken(SessionDTO session) {
        UserEntity user = userService.getUser(session);
        user.deleteSessionToken(session.sessionToken());
    }
}
