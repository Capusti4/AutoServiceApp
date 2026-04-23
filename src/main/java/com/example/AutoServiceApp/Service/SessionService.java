package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectSession;
import com.example.AutoServiceApp.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final UserRepository userRepository;

    public SessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUser(HttpSession session) {
        checkSession(session);
        return (UserDTO) session.getAttribute("user");
    }

    private void checkSession(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new IncorrectSession();
        }
        UserEntity entity = userRepository.findByUsername(user.username());
        if (entity == null) {
            throw new IncorrectSession();
        }
    }
}
