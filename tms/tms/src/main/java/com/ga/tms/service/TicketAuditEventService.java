package com.ga.tms.service;

import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.Ticket;
import com.ga.tms.model.TicketAuditEvent;
import com.ga.tms.model.User;
import com.ga.tms.repository.TicketAuditEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketAuditEventService {

    private final TicketAuditEventRepository ticketAuditEventRepository;

    @Autowired
    public TicketAuditEventService(TicketAuditEventRepository ticketAuditEventRepository) {
        this.ticketAuditEventRepository = ticketAuditEventRepository;
    }

    public TicketAuditEvent logEvent(Ticket ticket, String eventType, User actor,
                                     String oldValueJson, String newValueJson, String metadataJson) {
        TicketAuditEvent event = TicketAuditEvent.builder()
                .ticket(ticket)
                .eventType(eventType)
                .actor(actor)
                .oldValueJson(oldValueJson)
                .newValueJson(newValueJson)
                .metadataJson(metadataJson)
                .build();
        return ticketAuditEventRepository.save(event);
    }

    public List<TicketAuditEvent> getAuditTrailByTicket(Long ticketId) {
        return ticketAuditEventRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    public List<TicketAuditEvent> getEventsByActor(Long actorId) {
        return ticketAuditEventRepository.findByActorId(actorId);
    }

    public List<TicketAuditEvent> getEventsByType(String eventType) {
        return ticketAuditEventRepository.findByEventType(eventType);
    }

    public TicketAuditEvent getEventById(Long id) {
        return ticketAuditEventRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Audit event with id " + id + " not found."));
    }
}
