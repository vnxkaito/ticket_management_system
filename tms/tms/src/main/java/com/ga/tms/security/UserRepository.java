package com.ga.tms.security;

import com.ga.tms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
