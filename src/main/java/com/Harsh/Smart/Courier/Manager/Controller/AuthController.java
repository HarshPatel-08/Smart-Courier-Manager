package com.Harsh.Smart.Courier.Manager.Controller;

import com.Harsh.Smart.Courier.Manager.Dto.ApiResponse;
import com.Harsh.Smart.Courier.Manager.Dto.LoginRequest;
import com.Harsh.Smart.Courier.Manager.Dto.LoginResponse;
import com.Harsh.Smart.Courier.Manager.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok().body(new ApiResponse<>(true,"Login successful",response,
                HttpStatus.OK.value()));
    }
}
