package com.ga.tms.service;

import com.ga.tms.model.SlaEvent;
import com.ga.tms.model.Ticket;
import com.ga.tms.repository.SlaEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SlaEventService {
    private final SlaEventRepository slaEventRepository;

    @Autowired
    public SlaEventService(SlaEventRepository slaEventRepository){
        this.slaEventRepository = slaEventRepository;
    }

    public SlaEvent createSlaEvent(Ticket ticket, String eventType, LocalDateTime dueAt){
        SlaEvent slaEvent = SlaEvent.builder()
                .ticket(ticket)
                .eventType(eventType)
                .dueAt(dueAt)
                .status("PENDING")
                .build();
        return slaEventRepository.save(slaEvent);
    }

}
