package com.ga.tms.controller;

import com.ga.tms.model.TicketAuditEvent;
import com.ga.tms.service.TicketAuditEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/audit")
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


}
