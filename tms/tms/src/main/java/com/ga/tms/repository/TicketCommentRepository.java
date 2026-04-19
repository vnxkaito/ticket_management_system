package com.ga.tms.repository;

import com.ga.tms.model.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicketId(Long ticketId);
    List<TicketComment> findByTicketIdAndInternalOnly(Long ticketId, Boolean internalOnly);
}
