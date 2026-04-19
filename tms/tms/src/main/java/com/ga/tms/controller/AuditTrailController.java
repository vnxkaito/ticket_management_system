package com.ga.tms.controller;

import com.ga.tms.service.TicketAuditEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/audit")
public class AuditTrailController {
    private final TicketAuditEventService ticketAuditEventService;

    @Autowired
    public AuditTrailController(TicketAuditEventService ticketAuditEventService) {
        this.ticketAuditEventService = ticketAuditEventService;
    }
}
