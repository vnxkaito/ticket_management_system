package com.ga.tms.service;

import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.Ticket;
import com.ga.tms.model.TicketComment;
import com.ga.tms.model.User;
import com.ga.tms.repository.TicketCommentRepository;
import com.ga.tms.repository.TicketRepository;
import com.ga.tms.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCommentService {

    private final TicketCommentRepository ticketCommentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketCommentService(TicketCommentRepository ticketCommentRepository,
                                TicketRepository ticketRepository,
                                UserRepository userRepository) {
        this.ticketCommentRepository = ticketCommentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketComment addComment(Long ticketId, Long authorId, String body, String commentType, Boolean internalOnly) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new InformationNotFoundException("No ticket found with id " + ticketId));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new InformationNotFoundException("Author not found: " + authorId));

        TicketComment comment = TicketComment.builder()
                .ticket(ticket)
                .author(author)
                .body(body)
                .commentType(commentType)
                .internalOnly(internalOnly)
                .createdAt(LocalDateTime.now())
                .build();

        TicketComment savedComment = ticketCommentRepository.save(comment);

        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);

        return savedComment;
    }

    public List<TicketComment> getCommentsByTicket(Long ticketId) {
        return ticketCommentRepository.findByTicketId(ticketId);
    }

    public List<TicketComment> getPublicCommentsByTicket(Long ticketId) {
        return ticketCommentRepository.findByTicketIdAndInternalOnly(ticketId, false);
    }

    public List<TicketComment> getInternalCommentsByTicket(Long ticketId) {
        return ticketCommentRepository.findByTicketIdAndInternalOnly(ticketId, true);
    }

    public TicketComment getCommentById(Long id) {
        return ticketCommentRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Comment " + id + " doesn't exist"));
    }

    @Transactional
    public TicketComment updateComment(Long commentId, String newBody) {
        TicketComment comment = getCommentById(commentId);
        comment.setBody(newBody);
        return ticketCommentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        TicketComment comment = getCommentById(commentId);
        ticketCommentRepository.delete(comment);
    }
}
