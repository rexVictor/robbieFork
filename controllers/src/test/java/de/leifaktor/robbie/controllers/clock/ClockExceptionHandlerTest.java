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
import de.leifaktor.robbie.api.controllers.clock.ClockException;
import de.leifaktor.robbie.api.controllers.clock.ClockListener;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Tests the ClockExceptionHandler.
 */
public class ClockExceptionHandlerTest {

    /**
     * A stub implementation of clock.
     */
    private static class ClockStub implements Clock {


        /**
         * Empty Constructor.
         */
        ClockStub() {
        }

        @Override
        public void addClockListener(ClockListener listener) {

        }

        @Override
        public void setTickDuration(long duration, TimeUnit timeUnit) {

        }

        @Override
        public void startClock() {

        }

        @Override
        public void stopClock() throws ClockException {

        }

        @Override
        public boolean state() throws ClockException {
            return false;
        }

        @Override
        public long getTickDurationInMillis() {
            return 0;
        }
    }

    /**
     * A mock implementation of ClockRestorer.
     *
     * <p>On call it sets the AtomicBoolean to true and signal the condition.
     */
    private static final class MockRestorer implements ClockRestorer {

        /**
         * The lock providing the condition.
         */
        private final Lock lock;

        /**
         * The Condition to signal on call.
         */
        private final Condition condition;

        /**
         * The AtomicBoolean to set to true.
         */
        private final AtomicBoolean atomicBoolean;

        /**
         * Creates a new MockRestorer.
         * @param lock the lock providing condition
         * @param condition the condition to signal
         * @param atomicBoolean the boolean to update
         */
        private MockRestorer(Lock lock, Condition condition, AtomicBoolean atomicBoolean) {
            this.lock = lock;
            this.condition = condition;
            this.atomicBoolean = atomicBoolean;
        }

        @Override
        public void exceptionHappened(Clock clock) {
            lock.lock();
            try {
                atomicBoolean.set(true);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
    /**
     * The ExecutorService the tests use.
     */
    private ExecutorService service;

    /**
     * The clock the tests use.
     */
    private Clock clock;

    /**
     * Helper instance used to indicate if something got called.
     */
    private AtomicBoolean gotCalled;

    /**
     * Helper instance to lock for multi threading.
     */
    private Lock lock;

    /**
     * A condition of lock.
     */
    private Condition condition;

    /**
     * A mockRestorer.
     */
    private MockRestorer mockRestorer;

    /**
     * Empty Constructor.
     */
    public ClockExceptionHandlerTest() {
    }

    /**
     * Initializes the instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariables() {
        service = Executors.newCachedThreadPool();
        clock = new ClockStub();
        gotCalled = new AtomicBoolean(false);
        lock = new ReentrantLock();
        condition = lock.newCondition();
        mockRestorer = new MockRestorer(lock, condition, gotCalled);
    }

    /**
     * Shuts down the executor service if it is not null.
     */
    @AfterMethod
    public void tearDownService() {
        if (service != null) {
            service.shutdownNow();
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public void new_ServiceNull() {
        service = null;
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_ClockNull() {
        clock = null;
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_RestorerNull() {
        ClockRestorer restorer = null;
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(timeOut = 1000L)
    public void run_stopsOnInterruption() throws InterruptedException {
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);
        thread.start();
        thread.interrupt();
        thread.join();
    }

    @Test(timeOut = 1000L)
    public void run_StopsOnSleepingInterruption() throws InterruptedException {
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }
        thread.interrupt();
        thread.join();
    }

    @Test(timeOut = 1000L)
    public void run_notifiesRestorer_IfFirstExceptionOccurred() throws InterruptedException {
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, mockRestorer);
        Thread thread = new Thread(handler);

        handler.exceptionOccurred();
        thread.start();
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }

        Assert.assertTrue(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void run_notifiesRestorer_IfFirstRun() throws InterruptedException {
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, mockRestorer);
        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }

        handler.exceptionOccurred();
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }

        Assert.assertTrue(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void run_notRun_ifShutdown() throws InterruptedException {
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, mockRestorer);

        handler.shutdown();
        Thread thread = new Thread(handler);
        thread.start();
        thread.join();

        Assert.assertFalse(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void shutdown() throws InterruptedException {
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, mockRestorer);

        handler.shutdown();
        Thread thread = new Thread(handler);
        thread.start();
        thread.join();

        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());

    }

    @Test(timeOut = 1000L)
    public void run_terminatesOnShutdownWithoutCalling() throws InterruptedException {
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, mockRestorer);

        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }

        handler.shutdown();

        thread.join();

        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());

    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */