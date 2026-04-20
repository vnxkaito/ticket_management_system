package com.ga.tms.controller;

import com.ga.tms.model.TicketComment;
import com.ga.tms.security.MyUserDetails;
import com.ga.tms.service.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ga.tms.security.Roles;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
public class TicketCommentController {
    private static final String FIELD_BODY = "body";
    private static final String FIELD_COMMENT_TYPE = "commentType";
    private static final String FIELD_INTERNAL_ONLY = "internalOnly";
    private static final String COMMENT_TYPE_PUBLIC = "PUBLIC";
    private static final String COMMENT_TYPE_INTERNAL = "INTERNAL";

    private final TicketCommentService ticketCommentService;

    @Autowired
    public TicketCommentController(TicketCommentService ticketCommentService){
        this.ticketCommentService = ticketCommentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TicketComment> addComment(@PathVariable Long ticketId,
                                                    @RequestBody Map<String, Object> request,
                                                    @AuthenticationPrincipal MyUserDetails myUserDetails){
        Long authorId = myUserDetails.getUser().getId();
        String body = request.get(FIELD_BODY).toString();
        String commentType;
        if (request.get(FIELD_COMMENT_TYPE) != null) {
            commentType = request.get(FIELD_COMMENT_TYPE).toString();
        } else {
            commentType = COMMENT_TYPE_PUBLIC;
        }
        Boolean internalOnly = request.get(FIELD_INTERNAL_ONLY) != null && Boolean.parseBoolean(request.get(FIELD_INTERNAL_ONLY).toString());
        return ResponseEntity.ok(ticketCommentService.addComment(ticketId, authorId, body, commentType, internalOnly));
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @GetMapping
    public ResponseEntity<List<TicketComment>> getCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getCommentsByTicket(ticketId));
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @GetMapping("/internal")
    public ResponseEntity<List<TicketComment>> getInternalCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getInternalCommentsByTicket(ticketId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/public")
    public ResponseEntity<List<TicketComment>> getPublicCommentsByTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketCommentService.getPublicCommentsByTicket(ticketId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}")
    public ResponseEntity<TicketComment> getCommentById(@PathVariable Long commentId){
        return ResponseEntity.ok(ticketCommentService.getCommentById(commentId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId}")
    public ResponseEntity<TicketComment> updateComment(@PathVariable Long commentId,
                                                       @RequestBody Map<String, Object> request) {
        String newBody = request.get(FIELD_BODY).toString();
        return ResponseEntity.ok(ticketCommentService.updateComment(commentId, newBody));
    }

    @PreAuthorize("hasAnyRole('" + Roles.ADMIN + "', '" + Roles.AGENT + "')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        ticketCommentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
