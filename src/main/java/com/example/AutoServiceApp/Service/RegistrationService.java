package com.example.AutoServiceApp.Service;

import com.example.AutoServiceApp.DTO.UserDTO;
import com.example.AutoServiceApp.Entity.UserEntity;
import com.example.AutoServiceApp.Exception.IncorrectPhoneNumber;
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
        user.setPhoneNumber(formatPhoneNumber(user.getPhoneNumber()));
        userRepository.save(user);
        return new UserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isWorker()
        );
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IncorrectPhoneNumber();
        }

        String digits = phone.replaceAll("\\D+", "");

        if (digits.length() == 11 && digits.startsWith("8")) {
            digits = "7" + digits.substring(1);
        }

        if (digits.length() == 11 && digits.startsWith("7")) {
            return digits.replaceFirst("7(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+7 $1 ($2) $3-$4");
        }

        throw new IncorrectPhoneNumber();
    }
}
