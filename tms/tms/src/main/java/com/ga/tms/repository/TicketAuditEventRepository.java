package com.ga.tms.repository;

import com.ga.tms.model.TicketAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAuditEventRepository extends JpaRepository<TicketAuditEvent, Long> {
}
