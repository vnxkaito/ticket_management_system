package com.ga.tms.service;

import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.SlaEvent;
import com.ga.tms.model.Ticket;
import com.ga.tms.repository.SlaEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlaEventService {

    private final SlaEventRepository slaEventRepository;

    @Autowired
    public SlaEventService(SlaEventRepository slaEventRepository) {
        this.slaEventRepository = slaEventRepository;
    }

    public SlaEvent createSlaEvent(Ticket ticket, String eventType, LocalDateTime dueAt) {
        SlaEvent slaEvent = SlaEvent.builder()
                .ticket(ticket)
                .eventType(eventType)
                .dueAt(dueAt)
                .status("PENDING")
                .build();
        return slaEventRepository.save(slaEvent);
    }

    public List<SlaEvent> getSlaEventsByTicket(Long ticketId) {
        return slaEventRepository.findByTicketId(ticketId);
    }

    public List<SlaEvent> getPendingSlaEvents() {
        return slaEventRepository.findByStatus("PENDING");
    }

    public List<SlaEvent> getBreachedSlaEvents() {
        return slaEventRepository.findByStatusAndDueAtBefore("PENDING", LocalDateTime.now());
    }

    public SlaEvent markAsProcessed(Long slaEventId) {
        SlaEvent slaEvent = slaEventRepository.findById(slaEventId)
                .orElseThrow(() -> new InformationNotFoundException("SLA event " + slaEventId + " not found"));
        slaEvent.setStatus("PROCESSED");
        slaEvent.setProcessedAt(LocalDateTime.now());
        return slaEventRepository.save(slaEvent);
    }

    public SlaEvent markAsBreached(Long slaEventId) {
        SlaEvent slaEvent = slaEventRepository.findById(slaEventId)
                .orElseThrow(() -> new InformationNotFoundException("Couldn't find SLA event with id " + slaEventId));
        slaEvent.setStatus("BREACHED");
        slaEvent.setProcessedAt(LocalDateTime.now());
        return slaEventRepository.save(slaEvent);
    }

    public SlaEvent getSlaEventById(Long id) {
        return slaEventRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("No SLA event found with id " + id));
    }
}
