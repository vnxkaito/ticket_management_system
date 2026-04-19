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
                .orElseThrow(() -> new InformationNotFoundException("Customer not found: " + customerId));
        TicketCategory category = ticketCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new InformationNotFoundException("No category found with id " + categoryId));

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

        ticketAuditEventService.logEvent(savedTicket, "CREATED", customer); // no diffs on creation

        return savedTicket;
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Ticket " + id + " not found"));
    }

    public Ticket getTicketByExternalRef(String externalRef) {
        return ticketRepository.findByExternalRef(externalRef)
                .orElseThrow(() -> new InformationNotFoundException("No ticket found for ref: " + externalRef));
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
                .orElseThrow(() -> new InformationNotFoundException("Agent not found: " + agentId));

        String oldAgent = ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId().toString() : "null";
        ticket.setAssignedAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        String oldAgentVal = String.format("{\"assignedAgentId\":%s}", oldAgent);
        String newAgentVal = String.format("{\"assignedAgentId\":%d}", agentId);
        ticketAuditEventService.logEvent(ticket, "ASSIGNED_AGENT", actor, oldAgentVal, newAgentVal, null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket assignTicketToTeam(Long ticketId, Long teamId, User actor) {
        Ticket ticket = getTicketById(ticketId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new InformationNotFoundException("No team with id " + teamId));

        String oldTeam = ticket.getAssignedTeam() != null ? ticket.getAssignedTeam().getId().toString() : "null";
        ticket.setAssignedTeam(team);
        ticket.setUpdatedAt(LocalDateTime.now());

        String oldTeamVal = String.format("{\"assignedTeamId\":%s}", oldTeam);
        String newTeamVal = String.format("{\"assignedTeamId\":%d}", teamId);
        ticketAuditEventService.logEvent(ticket, "ASSIGNED_TEAM", actor, oldTeamVal, newTeamVal, null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket claimTicket(Long ticketId, Long agentId) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket.getAssignedAgent() != null) {
            throw new InformationExistException("Ticket is already assigned to an agent.");
        }

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new InformationNotFoundException("Agent " + agentId + " doesn't exist"));

        ticket.setAssignedAgent(agent);
        ticket.setUpdatedAt(LocalDateTime.now());

        String claimVal = String.format("{\"assignedAgentId\":%d}", agentId);
        ticketAuditEventService.logEvent(ticket, "CLAIMED", agent, null, claimVal, null);

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

        String oldStatusVal = String.format("{\"status\":\"%s\"}", oldStatus);
        String newStatusVal = String.format("{\"status\":\"%s\"}", newStatus);
        ticketAuditEventService.logEvent(ticket, "STATUS_CHANGED", actor, oldStatusVal, newStatusVal, null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket escalateTicket(Long ticketId, User actor) {
        Ticket ticket = getTicketById(ticketId);
        int oldLevel = ticket.getEscalatedLevel();
        ticket.setEscalatedLevel(oldLevel + 1);
        ticket.setUpdatedAt(LocalDateTime.now());

        String oldLevelVal = String.format("{\"escalatedLevel\":%d}", oldLevel);
        String newLevelVal = String.format("{\"escalatedLevel\":%d}", oldLevel + 1);
        ticketAuditEventService.logEvent(ticket, "ESCALATED", actor, oldLevelVal, newLevelVal, null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket reassignTicket(Long ticketId, Long newAgentId, Long newTeamId, User actor) {
        Ticket ticket = getTicketById(ticketId);

        if (newAgentId != null) {
            User newAgent = userRepository.findById(newAgentId)
                    .orElseThrow(() -> new InformationNotFoundException("Couldn't find agent with id " + newAgentId));
            ticket.setAssignedAgent(newAgent);
        }

        if (newTeamId != null) {
            Team newTeam = teamRepository.findById(newTeamId)
                    .orElseThrow(() -> new InformationNotFoundException("Team " + newTeamId + " not found"));
            ticket.setAssignedTeam(newTeam);
        }

        ticket.setUpdatedAt(LocalDateTime.now());

        ticketAuditEventService.logEvent(ticket, "REASSIGNED", actor); // no detailed diff needed

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
