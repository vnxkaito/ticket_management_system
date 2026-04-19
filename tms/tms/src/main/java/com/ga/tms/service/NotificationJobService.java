package com.ga.tms.service;

import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.model.NotificationJob;
import com.ga.tms.repository.NotificationJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationJobService {
    private final NotificationJobRepository notificationJobRepository;

    @Autowired
    public NotificationJobService(NotificationJobRepository notificationJobRepository){
        this.notificationJobRepository = notificationJobRepository;
    }

    public NotificationJob createNotificationJob(String aggregateType, Long aggregateId,
                                                 String channel, String recipient,
                                                 String payloadJson){
        LocalDateTime now = LocalDateTime.now();
        NotificationJob job = NotificationJob.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .channel(channel)
                .recipient(recipient)
                .payloadJson(payloadJson)
                .status("PENDING")
                .retryCount(0)
                .nextAttemptAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return notificationJobRepository.save(job);
    }

    public List<NotificationJob> getPendingJobs(){
        return notificationJobRepository.findByStatus("PENDING");
    }

    public List<NotificationJob> getJobsReadyForRetry() {
        return notificationJobRepository.findByStatusAndNextAttemptAtBefore("PENDING", LocalDateTime.now());
    }

    public List<NotificationJob> getJobsByAggregate(String aggregateType, Long aggregateId){
        return notificationJobRepository.findByAggregateTypeAndAggregateId(aggregateType, aggregateId);
    }

    public List<NotificationJob> getJobsByRecipient(String recipient){
        return notificationJobRepository.findByRecipient(recipient);
    }

    public NotificationJob getJobById(Long id){
        return notificationJobRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Notification job " + id + " not found"));
    }

    public NotificationJob markAsSent(Long jobId){
        NotificationJob job = getJobById(jobId);
        job.setStatus("SENT");
        job.setUpdatedAt(LocalDateTime.now());
        return notificationJobRepository.save(job);
    }

    public NotificationJob markAsFailed(Long jobId, String error){
        NotificationJob job = getJobById(jobId);
        job.setRetryCount(job.getRetryCount() + 1);
        job.setLastError(error);
        job.setNextAttemptAt(LocalDateTime.now().plusMinutes(5L * job.getRetryCount()));

        if(job.getRetryCount() >= 5) {
            job.setStatus("FAILED");
        }

        return notificationJobRepository.save(job);

    }
}
