package com.ga.tms.repository;

import com.ga.tms.model.SlaEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlaEventRepository extends JpaRepository<SlaEvent, Long> {
    List<SlaEvent> findByTicketId(Long ticketId);
    List<SlaEvent> findByStatus(String status);
    List<SlaEvent> findByEventType(String eventType);
    List<SlaEvent> findByStatusAndDueAtBefore(String status, LocalDateTime datetime);
}
