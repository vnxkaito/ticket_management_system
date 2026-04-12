package com.ga.tms.controller;

import com.ga.tms.auth.model.LoginRequest;
import com.ga.tms.model.User;
import com.ga.tms.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verify(@PathVariable String token) {
        System.out.println("Controller calling verify()");
        if(userService.verify(token)){
            return ResponseEntity.ok("Email verified successfully");
        }else {
            return ResponseEntity.ok("Email verification failed");
        }
    }
}
