package com.Harsh.Smart.Courier.Manager.Controller;

import com.Harsh.Smart.Courier.Manager.Dto.ApiResponse;
import com.Harsh.Smart.Courier.Manager.Dto.RegisterRequest;
import com.Harsh.Smart.Courier.Manager.Dto.RegisterResponse;
import com.Harsh.Smart.Courier.Manager.Service.RegisterUserUseCase;
import com.Harsh.Smart.Courier.Manager.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterUserUseCase register;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){

        try {
            RegisterResponse response = register.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            true,
                            "User registered successfully",
                            response,
                            HttpStatus.CREATED.value()
                    ));
        } catch (Exception ex) {
            // Will be caught by GlobalExceptionHandler
            throw ex;
        }
    }
}
