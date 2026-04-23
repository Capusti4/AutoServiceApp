package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.PhoneNumberAlreadyExists;
import com.example.AutoServiceApp.Exception.UsernameAlreadyExists;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO register(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExists();
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExists();
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
