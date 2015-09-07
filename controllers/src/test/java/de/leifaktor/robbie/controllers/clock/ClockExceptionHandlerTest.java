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
import rex.palace.testhelp.ThreadTestUtils;

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
        public long getTickDuration(TimeUnit unit) {
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
     * An empty ClockRestorer.
     */
    private final ClockRestorer emptyRestorer = cl -> { };

    /**
     * A ClockExceptionHandler using the mockRestorer.
     */
    private ClockExceptionHandler mockHandler;

    /**
     * A ClockExceptionHandler using the emptyRestorer.
     */
    private ClockExceptionHandler emptyHandler;

    /**
     * A thread constructed with emptyHandler as Runnable.
     */
    private Thread emptyHandlerThread;

    /**
     * A thread constructed with mockHandler as Runnable.
     */
    private Thread mockHandlerThread;

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
        mockHandler = new ClockExceptionHandler(service, clock, mockRestorer);
        mockHandlerThread = new Thread(mockHandler);
        emptyHandler = new ClockExceptionHandler(service, clock, emptyRestorer);
        emptyHandlerThread = new Thread(emptyHandler);
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
        new ClockExceptionHandler(null, clock, emptyRestorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_ClockNull() {
        new ClockExceptionHandler(service, null, emptyRestorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_RestorerNull() {
        new ClockExceptionHandler(service, clock, null);
    }

    @Test(timeOut = 1000L)
    public void run_stopsOnInterruption() throws InterruptedException {
        emptyHandlerThread.start();

        emptyHandlerThread.interrupt();

        emptyHandlerThread.join();
    }

    @Test(timeOut = 1000L)
    public void run_StopsOnSleepingInterruption() throws InterruptedException {
        emptyHandlerThread.start();
        ThreadTestUtils.waitTillThreadInState(emptyHandlerThread, Thread.State.WAITING);

        emptyHandlerThread.interrupt();

        emptyHandlerThread.join();
    }

    @Test(timeOut = 1000L)
    public void run_notifiesRestorer_IfFirstExceptionOccurred() throws InterruptedException {
        mockHandler.exceptionOccurred();
        mockHandlerThread.start();

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
        mockHandlerThread.start();
        ThreadTestUtils.waitTillThreadInState(mockHandlerThread, Thread.State.WAITING);

        mockHandler.exceptionOccurred();
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
        mockHandler.shutdown();
        mockHandlerThread.start();

        mockHandlerThread.join();
        Assert.assertFalse(gotCalled.get());
    }

    @Test(timeOut = 1000L)
    public void shutdown() throws InterruptedException {
        mockHandler.shutdown();
        mockHandlerThread.start();
        mockHandlerThread.join();

        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());
    }

    @Test(timeOut = 1000L)
    public void run_terminatesOnShutdownWithoutCalling() throws InterruptedException {
        mockHandlerThread.start();
        ThreadTestUtils.waitTillThreadInState(mockHandlerThread, Thread.State.WAITING);

        mockHandler.shutdown();

        mockHandlerThread.join();
        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */