package com.ga.tms.service;

import com.ga.tms.exceptions.LockAcquisitionException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TicketLockManager {

    private static final long LOCK_TIMEOUT_SECONDS = 3;

    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void acquire(Long ticketId) {
        ReentrantLock lock = locks.computeIfAbsent(ticketId, id -> new ReentrantLock());
        try {
            if (!lock.tryLock(LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new LockAcquisitionException("Could not acquire lock for ticket " + ticketId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockAcquisitionException("Interrupted while waiting for lock on ticket " + ticketId);
        }
    }

    public void release(Long ticketId) {
        ReentrantLock lock = locks.get(ticketId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
