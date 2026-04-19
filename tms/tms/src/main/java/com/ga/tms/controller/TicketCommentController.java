package com.ga.tms.controller;

import com.ga.tms.model.TicketComment;
import com.ga.tms.security.MyUserDetails;
import com.ga.tms.service.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/tickets/{ticketId}/comments")
public class TicketCommentController {
    public final TicketCommentService ticketCommentService;

    @Autowired
    public TicketCommentController(TicketCommentService ticketCommentService){
        this.ticketCommentService = ticketCommentService;
    }

    @PostMapping
    public ResponseEntity<TicketComment> addComment(@PathVariable Long ticketId,
                                                    @RequestBody Map<String, Object> request,
                                                    @AuthenticationPrincipal MyUserDetails myUserDetails){
        Long authorId = myUserDetails.getUser().getId();
        String body = request.get("body").toString();
        String commentType = request.get("commentType") != null ? request.get("commentType").toString() : "PUBLIC";
        Boolean internalOnly = request.get("internalOnly") != null && Boolean.parseBoolean(request.get("internalOnly").toString());
        return ResponseEntity.ok(ticketCommentService.addComment(ticketId, authorId, body, commentType, internalOnly));
    }

    @GetMapping
    public ResponseEntity<List<TicketComment>> getCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getCommentsByTicket(ticketId));
    }

    @GetMapping("/internal")
    public ResponseEntity<List<TicketComment>> getInternalCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getInternalCommentsByTicket(ticketId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<TicketComment>> getPublicCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getPublicCommentsByTicket(ticketId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<TicketComment> getCommentById(@PathVariable Long commentId){
        return ResponseEntity.ok(ticketCommentService.getCommentById(commentId));
    }
}
