package com.ga.tms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sla_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "due_at", nullable = false)
    private LocalDateTime dueAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(nullable = false)
    private String status;
}
