package com.ga.tms.controller;

import com.ga.tms.model.Ticket;
import com.ga.tms.model.User;
import com.ga.tms.security.MyUserDetails;
import com.ga.tms.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Map<String, Object> request,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long customerId = myUserDetails.getUser().getId();
        Long categoryId = Long.valueOf(request.get("categoryId").toString());
        String subject = request.get("subject").toString();
        String description = request.get("description") != null ? request.get("description").toString() : null;
        return ResponseEntity.ok(ticketService.createTicket(customerId, categoryId, subject, description));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/ref/{externalRef}")
    public ResponseEntity<Ticket> getTicketByExternalRef(@PathVariable String externalRef) {
        return ResponseEntity.ok(ticketService.getTicketByExternalRef(externalRef));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long customerId = myUserDetails.getUser().getId();
        return ResponseEntity.ok(ticketService.getTicketsByCustomer(customerId));
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<Ticket>> getAssignedTickets(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long agentId = myUserDetails.getUser().getId();
        return ResponseEntity.ok(ticketService.getTicketsByAgent(agentId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Ticket>> getTicketsByTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(ticketService.getTicketsByTeam(teamId));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Ticket>> filterTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long teamId) {
        if (status != null && teamId != null) {
            return ResponseEntity.ok(ticketService.getTicketsByStatusAndTeam(status, teamId));
        } else if (status != null) {
            return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
        } else if (priority != null) {
            return ResponseEntity.ok(ticketService.getTicketsByPriority(priority));
        } else if (categoryId != null) {
            return ResponseEntity.ok(ticketService.getTicketsByCategory(categoryId));
        }
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PutMapping("/{ticketId}/claim")
    public ResponseEntity<Ticket> claimTicket(@PathVariable Long ticketId,
                                              @AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long agentId = myUserDetails.getUser().getId();
        return ResponseEntity.ok(ticketService.claimTicket(ticketId, agentId));
    }

    @PutMapping("/{ticketId}/assign/agent/{agentId}")
    public ResponseEntity<Ticket> assignTicketToAgent(@PathVariable Long ticketId,
                                                      @PathVariable Long agentId,
                                                      @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User actor = myUserDetails.getUser();
        return ResponseEntity.ok(ticketService.assignTicketToAgent(ticketId, agentId, actor));
    }

    @PutMapping("/{ticketId}/assign/team/{teamId}")
    public ResponseEntity<Ticket> assignTicketToTeam(@PathVariable Long ticketId,
                                                     @PathVariable Long teamId,
                                                     @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User actor = myUserDetails.getUser();
        return ResponseEntity.ok(ticketService.assignTicketToTeam(ticketId, teamId, actor));
    }

    @PutMapping("/{ticketId}/reassign")
    public ResponseEntity<Ticket> reassignTicket(@PathVariable Long ticketId,
                                                 @RequestBody Map<String, Long> request,
                                                 @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User actor = myUserDetails.getUser();
        Long newAgentId = request.get("agentId");
        Long newTeamId = request.get("teamId");
        return ResponseEntity.ok(ticketService.reassignTicket(ticketId, newAgentId, newTeamId, actor));
    }

    @PutMapping("/{ticketId}/status")
    public ResponseEntity<Ticket> changeStatus(@PathVariable Long ticketId,
                                               @RequestBody Map<String, String> request,
                                               @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User actor = myUserDetails.getUser();
        String newStatus = request.get("status");
        return ResponseEntity.ok(ticketService.changeStatus(ticketId, newStatus, actor));
    }

    @PutMapping("/{ticketId}/escalate")
    public ResponseEntity<Ticket> escalateTicket(@PathVariable Long ticketId,
                                                 @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User actor = myUserDetails.getUser();
        return ResponseEntity.ok(ticketService.escalateTicket(ticketId, actor));
    }
}
