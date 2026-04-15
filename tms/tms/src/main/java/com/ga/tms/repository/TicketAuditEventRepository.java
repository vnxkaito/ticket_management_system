package com.ga.tms.repository;

import com.ga.tms.model.TicketAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketAuditEventRepository extends JpaRepository<TicketAuditEvent, Long> {
    List<TicketAuditEvent> findByTicketId(Long ticketId);
    List<TicketAuditEvent> findByActorId(Long actorId);
    List<TicketAuditEvent> findByEventType(String eventType);
    List<TicketAuditEvent> findByTicketIdOrderByCreatedAtDesc(Long ticketId);
}

