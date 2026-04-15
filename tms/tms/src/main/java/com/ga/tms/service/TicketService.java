package com.ga.tms.service;

import com.ga.tms.exceptions.InformationExistException;
import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.*;
import com.ga.tms.repository.TicketCategoryRepository;
import com.ga.tms.repository.TicketRepository;
import com.ga.tms.repository.TeamRepository;
import com.ga.tms.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final SlaEventService slaEventService;
    private final TicketAuditEventService ticketAuditEventService;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         TicketCategoryRepository ticketCategoryRepository,
                         UserRepository userRepository,
                         TeamRepository teamRepository,
                         SlaEventService slaEventService,
                         TicketAuditEventService ticketAuditEventService) {
        this.ticketRepository = ticketRepository;
        this.ticketCategoryRepository = ticketCategoryRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.slaEventService = slaEventService;
        this.ticketAuditEventService = ticketAuditEventService;
    }

    @Transactional
    public Ticket createTicket(Long customerId, Long categoryId, String subject, String description) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new InformationNotFoundException("User with id " + customerId + " not found."));
        TicketCategory category = ticketCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new InformationNotFoundException("Category with id " + categoryId + " not found."));

        LocalDateTime now = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .externalRef("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .subject(subject)
                .description(description)
                .customer(customer)
                .category(category)
                .status("OPEN")
                .priority(category.getDefaultPriority())
                .escalatedLevel(0)
                .version(0)
                .firstResponseDueAt(now.plusMinutes(category.getFirstResponseSlaMinutes()))
                .resolutionDueAt(now.plusMinutes(category.getResolutionSlaMinutes()))
                .createdAt(now)
                .updatedAt(now)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        slaEventService.createSlaEvent(savedTicket, "FIRST_RESPONSE", savedTicket.getFirstResponseDueAt());
        slaEventService.createSlaEvent(savedTicket, "RESOLUTION", savedTicket.getResolutionDueAt());

        ticketAuditEventService.logEvent(savedTicket, "CREATED", customer, null, null, null);

        return savedTicket;
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Ticket with id " + id + " not found."));
    }

    public Ticket getTicketByExternalRef(String externalRef) {
        return ticketRepository.findByExternalRef(externalRef)
                .orElseThrow(() -> new InformationNotFoundException("Ticket with ref " + externalRef + " not found."));
    }

    public List<Ticket> getTicketsByCustomer(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    public List<Ticket> getTicketsByAgent(Long agentId) {
        return ticketRepository.findByAssignedAgentId(agentId);
    }

    public List<Ticket> getTicketsByTeam(Long teamId) {
        return ticketRepository.findByAssignedTeamId(teamId);
    }

    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> getTicketsByPriority(String priority) {
        return ticketRepository.findByPriority(priority);
    }

    public List<Ticket> getTicketsByCategory(Long categoryId) {
        return ticketRepository.findByCategoryId(categoryId);
    }

    public List<Ticket> getTicketsByStatusAndTeam(String status, Long teamId) {
        return ticketRepository.findByStatusAndAssignedTeamId(status, teamId);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket assignTicketToAgent(Long ticketId, Long agentId, User actor) {
        Ticket ticket = getTicketById(ticketId);
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new InformationNotFoundException("User with id " + agentId + " not found."));

        String oldAgent = ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId().toString() : "null";
        ticket.setAssignedAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "ASSIGNED_AGENT", actor,
                "{\"assignedAgentId\":" + oldAgent + "}",
                "{\"assignedAgentId\":" + agentId + "}", null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket assignTicketToTeam(Long ticketId, Long teamId, User actor) {
        Ticket ticket = getTicketById(ticketId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new InformationNotFoundException("Team with id " + teamId + " not found."));

        String oldTeam = ticket.getAssignedTeam() != null ? ticket.getAssignedTeam().getId().toString() : "null";
        ticket.setAssignedTeam(team);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "ASSIGNED_TEAM", actor,
                "{\"assignedTeamId\":" + oldTeam + "}",
                "{\"assignedTeamId\":" + teamId + "}", null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket claimTicket(Long ticketId, Long agentId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket.getAssignedAgent() != null) {
            throw new InformationExistException("Ticket is already assigned to an agent.");
        }

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new InformationNotFoundException("User with id " + agentId + " not found."));

        ticket.setAssignedAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "CLAIMED", agent, null,
                "{\"assignedAgentId\":" + agentId + "}", null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket changeStatus(Long ticketId, String newStatus, User actor) {
        Ticket ticket = getTicketById(ticketId);
        String oldStatus = ticket.getStatus();

        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());

        if ("RESOLVED".equals(newStatus)) {
            ticket.setResolvedAt(LocalDateTime.now());
        }

        ticketAuditEventService.logEvent(ticket, "STATUS_CHANGED", actor,
                "{\"status\":\"" + oldStatus + "\"}",
                "{\"status\":\"" + newStatus + "\"}", null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket escalateTicket(Long ticketId, User actor) {
        Ticket ticket = getTicketById(ticketId);
        int oldLevel = ticket.getEscalatedLevel();
        ticket.setEscalatedLevel(oldLevel + 1);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "ESCALATED", actor,
                "{\"escalatedLevel\":" + oldLevel + "}",
                "{\"escalatedLevel\":" + ticket.getEscalatedLevel() + "}", null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket reassignTicket(Long ticketId, Long newAgentId, Long newTeamId, User actor) {
        Ticket ticket = getTicketById(ticketId);

        if (newAgentId != null) {
            User newAgent = userRepository.findById(newAgentId)
                    .orElseThrow(() -> new InformationNotFoundException("User with id " + newAgentId + " not found."));
            ticket.setAssignedAgent(newAgent);
        }

        if (newTeamId != null) {
            Team newTeam = teamRepository.findById(newTeamId)
                    .orElseThrow(() -> new InformationNotFoundException("Team with id " + newTeamId + " not found."));
            ticket.setAssignedTeam(newTeam);
        }

        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "REASSIGNED", actor, null, null, null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket recordFirstResponse(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket.getFirstRespondedAt() == null) {
            ticket.setFirstRespondedAt(LocalDateTime.now());
            ticket.setUpdatedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        }
        return ticket;
    }
}