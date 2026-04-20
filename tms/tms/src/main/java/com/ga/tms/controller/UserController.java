package com.ga.tms.controller;

import com.ga.tms.auth.model.LoginRequest;
import com.ga.tms.auth.model.LoginResponse;
import com.ga.tms.model.User;
import com.ga.tms.security.MyUserDetails;
import com.ga.tms.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        userService.forgotPassword(request.get("email"));
        return ResponseEntity.ok("If that email is registered, a reset link has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        userService.resetPassword(request.get("token"), request.get("newPassword"));
        return ResponseEntity.ok("Password reset successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request,
                                                  @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long userId = myUserDetails.getUser().getId();
        userService.changePassword(userId, request.get("currentPassword"), request.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, String> request,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long userId = myUserDetails.getUser().getId();
        return ResponseEntity.ok(userService.updateProfile(userId, request.get("fullName"), request.get("email")));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                        @AuthenticationPrincipal MyUserDetails myUserDetails) throws IOException {
        Long userId = myUserDetails.getUser().getId();
        userService.uploadProfilePicture(userId, file.getBytes());
        return ResponseEntity.ok("Profile picture uploaded successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/picture")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long userId = myUserDetails.getUser().getId();
        byte[] picture = userService.getProfilePicture(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(picture);
    }
}
