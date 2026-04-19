package com.ga.tms.controller;

import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.User;
import com.ga.tms.security.UserRepository;
import com.ga.tms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User with id " + userId + " not found."));
        user.setActive(true);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User with id " + userId + " not found."));
        user.setActive(false);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<User> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.assignRoleToUser(userId, roleId));
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(roleService.removeRoleFromUser(userId, roleId));
    }
}
