package com.ga.tms.repository;

import com.ga.tms.model.NotificationJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationJobRepository extends JpaRepository<NotificationJob, Long> {
    List<NotificationJob> findByStatus(String status);
    List<NotificationJob> findByAggregateTypeAndAggregateId(String aggregateType, Long aggregateId);
    List<NotificationJob> findByStatusAndNextAttemptAtBefore(String status, LocalDateTime dateTime);
    List<NotificationJob> findByRecipient(String recipient);
}
