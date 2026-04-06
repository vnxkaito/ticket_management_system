package com.ga.tms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "default_priority", nullable = false)
    private String defaultPriority;

    @Column(name = "first_response_sla_minutes", nullable = false)
    private Integer firstResponseSlaMinutes;

    @Column(name = "resolution_sla_minutes", nullable = false)
    private Integer resolutionSlaMinutes;

    @Column(name = "routing_strategy", nullable = false)
    private String routingStrategy;
}