package com.Harsh.Smart.Courier.Manager.Service;

import com.Harsh.Smart.Courier.Manager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


}
