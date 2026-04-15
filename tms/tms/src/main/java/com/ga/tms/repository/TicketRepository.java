package com.ga.tms.repository;

import com.ga.tms.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByExternalRef(String externalRef);
    List<Ticket> findByCustomerId(Long customerId);
    List<Ticket> findByAssignedAgentId(Long agentId);
    List<Ticket> findByAssignedTeamId(Long teamId);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByPriority(String priority);
    List<Ticket> findByCategoryId(Long categoryId);
    List<Ticket> findByStatusAndAssignedTeamId(String status, Long teamId);
    List<Ticket> findByStatusAndAssignedAgentId(String status, Long agentId);
}
