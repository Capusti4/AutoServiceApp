package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.RegisterResponse;
import com.example.AutoServiceApp.DTO.SessionDTO;
import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.PhoneNumberAlreadyExists;
import com.example.AutoServiceApp.Exception.UsernameAlreadyExists;
import com.example.AutoServiceApp.Repository.RegistrationRepository;
import com.example.AutoServiceApp.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final RegistrationRepository repository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public RegisterResponse register(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExists();
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExists();
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
        repository.save(user);
        return new RegisterResponse(
                "Регистрация прошла успешно",
                userDTO,
                sessionDTO
        );
    }
}
