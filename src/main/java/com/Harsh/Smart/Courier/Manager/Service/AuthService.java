package com.Harsh.Smart.Courier.Manager.Service;

import com.Harsh.Smart.Courier.Manager.Dto.LoginRequest;
import com.Harsh.Smart.Courier.Manager.Dto.LoginResponse;
import com.Harsh.Smart.Courier.Manager.Exception.InvalidCredentialsException;
import com.Harsh.Smart.Courier.Manager.Model.Users;
import com.Harsh.Smart.Courier.Manager.Repository.UserRepository;
import com.Harsh.Smart.Courier.Manager.Security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger logger= LoggerFactory.getLogger(AuthService.class);

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for email: {}",request.getEmail());

        String email= request.getEmail().trim().toLowerCase();
        Users user = userRepository.findByEmail(email).orElseThrow(()->{
            logger.warn("Login failed: User not found  - {}",email);
            return new InvalidCredentialsException("Invalid email and password");
        });
        if(!encoder.matches(request.getPassword(),user.getPassword())){
            logger.warn("Login failed: Wrong password for - {}",email);
            throw new InvalidCredentialsException("Invalid email and password");
        }
        String token = jwtTokenProvider.generateToken(user.getId(),
                user.getEmail(),
                user.getRole().name());
        logger.info("User logged in successfully: {} ({})",user.getName(),user.getRole());

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}
