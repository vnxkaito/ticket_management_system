package com.ga.tms.seed;

import com.ga.tms.model.Role;
import com.ga.tms.model.TicketCategory;
import com.ga.tms.model.User;
import com.ga.tms.repository.RoleRepository;
import com.ga.tms.repository.TicketCategoryRepository;
import com.ga.tms.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TicketCategoryRepository ticketCategoryRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      TicketCategoryRepository ticketCategoryRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        seedAdminUser();
        seedCategories();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name("ADMIN").build());
            roleRepository.save(Role.builder().name("AGENT").build());
            roleRepository.save(Role.builder().name("CUSTOMER").build());
        }
    }

    private void seedAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found during seeding"));

            User admin = User.builder()
                    .username("admin")
                    .email("admin@tms.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .active(true)
                    .fullName("System Admin")
                    .createdAt(LocalDateTime.now())
                    .roles(new HashSet<>(Set.of(adminRole)))
                    .teams(new HashSet<>())
                    .build();

            userRepository.save(admin);
        }
    }

    private void seedCategories() {
        if (ticketCategoryRepository.count() == 0) {
            ticketCategoryRepository.save(TicketCategory.builder()
                    .name("Technical Support")
                    .defaultPriority("HIGH")
                    .build());
            ticketCategoryRepository.save(TicketCategory.builder()
                    .name("Billing")
                    .defaultPriority("MEDIUM")
                    .build());
            ticketCategoryRepository.save(TicketCategory.builder()
                    .name("General Inquiry")
                    .defaultPriority("LOW")
                    .build());
        }
    }
}
