/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Robbie.
 *
 * Robbie is a 2d-adventure game.
 * Copyright (C) 2015 Matthias Johannes Reimchen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.leifaktor.robbie.controllers.clock;

import de.leifaktor.robbie.api.controllers.clock.Clock;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles Exceptions during Clock execution.
 */
public class ClockExceptionHandler implements Runnable {

    /**
     * The Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClockExceptionHandler.class);

    /**
     * The ExecutorService running the ClockRestorer.
     */
    private final ExecutorService service;

    /**
     * The lock providing the condition object.
     */
    private final Lock lock = new ReentrantLock();

    /**
     * The condition this sleeps on.
     */
    private final Condition condition = lock.newCondition();

    /**
     * The Thread calling the actual ClockRestorer.
     */
    private final Runnable clockRestorer;

    /**
     * If this runnable should terminate.
     */
    private boolean shutdown = false;

    /**
     * Indicates that exceptionOccurred has been called,
     * but the ClockRestorer has not yet been called.
     *
     * <p>Needed for Race conditions, if exceptionOccurred() is scheduled
     * before run() can acquire the lock.
     */
    private boolean exceptionOccurred = false;

    /**
     * Creates a new ClockExceptionHandler.
     *
     * @param service the ExecutorService to use
     * @param clock the clock to handle
     * @param restorer the ClockRestorer to notify
     */
    public ClockExceptionHandler(ExecutorService service, Clock clock, ClockRestorer restorer) {
        this.service = Objects.requireNonNull(service, "The ExecutorService may not be null.");
        Objects.requireNonNull(clock, "The Clock may not be null.");
        Objects.requireNonNull(restorer, "The ClockRestorer may not be null.");
        this.clockRestorer = () -> restorer.exceptionHappened(clock, null) ;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !shutdown) {
            lock.lock();
            try {
                if (!exceptionOccurred) {
                    condition.await();
                }
            } catch (InterruptedException e) {
                LOGGER.error("Got Interrupted waiting for condition.", e);
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                exceptionOccurred = false;
                submit();
            }
        }
    }

    /**
     * Submits the task if shutdown is not triggered.
     */
    private void submit() {
        if (!shutdown) {
            service.submit(clockRestorer);
        }
    }

    /**
     * Shuts down this ClockExceptionHandler.
     */
    public void shutdown() {
        lock.lock();
        try {
            shutdown = true;
            condition.signalAll();
        } finally {
            lock.unlock();
            service.shutdown();
            service.shutdownNow();
        }
    }

    /**
     * Notifies that an exception occurred.
     */
    public void exceptionOccurred() {
        lock.lock();
        try {
            exceptionOccurred = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
