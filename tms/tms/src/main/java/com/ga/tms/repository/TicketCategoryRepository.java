package com.ga.tms.repository;

import com.ga.tms.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    Optional<TicketCategory> findByName(String name);
    boolean existsByName(String name);
}
