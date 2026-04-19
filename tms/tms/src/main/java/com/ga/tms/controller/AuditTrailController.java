package com.ga.tms.controller;

import com.ga.tms.model.TicketAuditEvent;
import com.ga.tms.service.TicketAuditEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
public class AuditTrailController {
    private final TicketAuditEventService ticketAuditEventService;

    @Autowired
    public AuditTrailController(TicketAuditEventService ticketAuditEventService) {
        this.ticketAuditEventService = ticketAuditEventService;
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<List<TicketAuditEvent>> getAuditTrailByTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketAuditEventService.getAuditTrailByTicket(ticketId));
    }

    @GetMapping("/actors/{actorId}")
    public ResponseEntity<List<TicketAuditEvent>> getEventsByActor(@PathVariable Long actorId) {
        return ResponseEntity.ok(ticketAuditEventService.getEventsByActor(actorId));
    }

    @GetMapping("/events/{eventType}")
    public ResponseEntity<List<TicketAuditEvent>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(ticketAuditEventService.getEventsByType(eventType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketAuditEvent> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketAuditEventService.getEventById(id));
    }


}
