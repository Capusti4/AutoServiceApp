package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectSession;
import com.example.AutoServiceApp.Exception.IncorrectUserType;
import com.example.AutoServiceApp.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUser(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new IncorrectSession();
        }
        return getEntity(user);
    }

    public UserEntity getWorker(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new IncorrectSession();
        }
        if (!user.isWorker()) {
            throw new IncorrectUserType();
        }
        return getEntity(user);
    }

    public UserEntity getClient(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            throw new IncorrectSession();
        }
        if (user.isWorker()) {
            throw new IncorrectUserType();
        }
        return getEntity(user);
    }

    private UserEntity getEntity(UserDTO user) {
        UserEntity userEntity = userRepository.findByUsername(user.username());
        if (userEntity == null) {
            throw new IncorrectSession();
        }
        return userEntity;
    }
}
