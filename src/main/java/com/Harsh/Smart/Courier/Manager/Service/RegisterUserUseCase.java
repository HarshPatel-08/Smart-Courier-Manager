package com.Harsh.Smart.Courier.Manager.Service;

import com.Harsh.Smart.Courier.Manager.Dto.RegisterRequest;
import com.Harsh.Smart.Courier.Manager.Dto.RegisterResponse;
import com.Harsh.Smart.Courier.Manager.Exception.PasswordMismatchException;
import com.Harsh.Smart.Courier.Manager.Exception.UserAlreadyExistsException;
import com.Harsh.Smart.Courier.Manager.Model.UserRole;
import com.Harsh.Smart.Courier.Manager.Model.Users;
import com.Harsh.Smart.Courier.Manager.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder encoder;

    @Autowired
    private EmailService emailService;
//       private final  UserRepository userRepository;
//        private  final BCryptPasswordEncoder encoder;
//    public RegisterUserUseCase(UserRepository userRepository,
//                               BCryptPasswordEncoder encoder) {
//        this.userRepository = userRepository;
//        this.encoder = encoder;
//    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Normalize input
        String name = request.getName().trim();
        String email = request.getEmail().trim().toLowerCase();

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Validate password match
        if (request.getConfirmPassword() == null ||
                !request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        // Restrict role
        UserRole role = UserRole.CUSTOMER;

        // Hash password
        String encodedPassword = encoder.encode(request.getPassword());

        // Create entity
        Users user = new Users(name, email, encodedPassword, role);

        // Save
        userRepository.save(user);

        // Email notification (async)
        emailService.sendWelcomeEmail(email,name,role.name());

        RegisterResponse users = new RegisterResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
        return users;
    }
}