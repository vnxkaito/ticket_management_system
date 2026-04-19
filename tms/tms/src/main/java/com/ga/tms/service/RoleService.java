package com.ga.tms.service;

import com.ga.tms.exceptions.InformationExistException;
import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.Role;
import com.ga.tms.model.User;
import com.ga.tms.repository.RoleRepository;
import com.ga.tms.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new InformationExistException("Role with name " + role.getName() + " already exists.");
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("No role found with id " + id));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new InformationNotFoundException("Role '" + name + "' not found"));
    }

    public Role updateRole(Long id, Role roleDetails) {
        Role role = getRoleById(id);
        role.setName(roleDetails.getName());
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }

    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User " + userId + " not found"));
        Role role = getRoleById(roleId);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    public User removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("Couldn't find user with id " + userId));
        Role role = getRoleById(roleId);
        user.getRoles().remove(role);
        return userRepository.save(user);
    }
}
