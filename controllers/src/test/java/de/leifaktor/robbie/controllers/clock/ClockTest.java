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
import de.leifaktor.robbie.api.controllers.clock.TicksTooFastException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import rex.palace.testes.SequentialScheduledExecutorService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Tests the Clock implementation.
 */
public class ClockTest {

    /**
     * A Thread helping testing.
     */
    private abstract static class TestThread extends Thread {

        /**
         * The exception thrown by run.
         */
        public Throwable exception;

        /**
         * A failMessage used for assertFail.
         */
        public String failMessage;

        /**
         * Creates a new TestThread.
         */
        TestThread() {
        }

        @Override
        public abstract void run();

        /**
         * Execute in current Thread.
         *
         * @throws Throwable if it occurred in run
         */
        public void finish() throws Throwable {
            if (failMessage != null) {
                Assert.fail(failMessage);
            }
            if (exception != null) {
                throw exception;
            }
        }

    }

    /**
     * A mock class for TickEventHandler.
     */
    private static class CallCounter implements TickEventHandler {

        /**
         * The number of times run() got called.
         */
        public int count = 0;

        /**
         * Creates a new TickEventHandlerMock.
         */
        CallCounter() {
        }

        @Override
        public void run() {
            count++;
        }

        @Override
        public boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                    throws TimeoutException, InterruptedException {
            return true;
        }

        @Override
        public boolean areDone() {
            return true;
        }

    }

    /**
     * A NOP implementation of TickEventHandler.
     */
    private static class NopTickEventHandler implements TickEventHandler {

        /**
         * The return vale of shutdown.
         */
        public boolean shutdown = false;

        /**
         * The return value of areDone.
         */
        public boolean areDone = true;


        /**
         * Creates a new TickEventHandlerMock.
         *
         */
        NopTickEventHandler() {
        }

        @Override
        public void run() {
        }

        @Override
        public boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                    throws TimeoutException, InterruptedException {
            return shutdown;
        }

        @Override
        public boolean areDone() {
            return areDone;
        }

    }

    /**
     * An Exception throwing implementation of TickEventHandler.
     */
    private static class ShutDownThrowHandler implements TickEventHandler {

        /**
         * The Exception to throw.
         */
        private final Throwable throwable;

        /**
         * Creates a new TickEventHandlerMock.
         *
         * @param throwable the exception to throw in run
         */
        ShutDownThrowHandler(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void run() {
        }

        @Override
        public boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                    throws TimeoutException, InterruptedException {
            try {
                throw throwable;
            } catch (TimeoutException | InterruptedException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean areDone() {
            return true;
        }

    }

    /**
     * The SequentialScheduledExecutorService to use.
     */
    private SequentialScheduledExecutorService sses;

    /**
     * A TickEventHandlerFactory producing null.
     */
    private final TickEventHandlerFactory nullFactory = set -> null;

    /**
     * The nop TickEventHandler.
     */
    private NopTickEventHandler nopHandler;

    /**
     * A Nop factory.
     */
    private final TickEventHandlerFactory nopFactory = set -> nopHandler;

    /**
     * The CallCounter tests can use.
     */
    private CallCounter callCounter;

    /**
     * Default Constructor.
     */
    public ClockTest() {
    }

    /**
     * Sets up instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariables() {
        sses = new SequentialScheduledExecutorService();
        callCounter = new CallCounter();
        nopHandler = new NopTickEventHandler();
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void new_NegativeTickDuration() {
        new ClockImpl(nullFactory, sses, -1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullExecutorService() {
        new ClockImpl(nullFactory, null, 1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullTickEventHandlerFactory() {
        new ClockImpl(null, sses, 1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullTimeUnit() {
        new ClockImpl(nullFactory, sses, 1L, null);
    }

    @Test
    public void getTickDurationInMillis() {
        Clock clock = new ClockImpl(nullFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        Assert.assertEquals(clock.getTickDurationInMillis(), 10L);
    }

    @Test
    public void setTickDuration_StoppedClock() {
        Clock clock = new ClockImpl(nullFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(5L, TimeUnit.SECONDS);

        Assert.assertEquals(clock.getTickDurationInMillis(), 5000L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setTickDuration_NegativeDuration_StoppedClock() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(-1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void setTickDuration_NullUnit_StoppedClock() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(1L, null);
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void setTickDuration_StartedClock() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        clock.setTickDuration(5L, TimeUnit.SECONDS);
    }

    @Test
    public void startClock_noExceptions() throws ClockException {
        sses.setCallCount(10);
        callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        Assert.assertEquals(callCounter.count, 10);
        Assert.assertTrue(clock.state());
    }

    @Test(expectedExceptions = TicksTooFastException.class)
    public void startClock_NotAllDone() throws ClockException {
        sses.setCallCount(1);
        nopHandler.areDone = false;
        Clock clock = new ClockImpl(nopFactory, sses,
                7L, TimeUnit.NANOSECONDS);

        clock.startClock();
        clock.state();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void state_interrupted() throws Throwable {
        sses.setCallCount(1);
        Clock clock = new ClockImpl(nopFactory, sses,
                7L, TimeUnit.NANOSECONDS);

        clock.startClock();
        clock.stopClock();

        TestThread thread = new TestThread() {

            @Override
            public void run() {
                interrupt();
                try {
                    clock.state();
                    failMessage = "IllegalStateException not thrown.";
                } catch (Throwable e) {
                    exception = e;
                }
            }
        };
        thread.start();
        thread.join();
        thread.finish();
    }

    @Test(expectedExceptions = ClockAlreadyStoppedException.class)
    public void stopClock_neverStarted() throws ClockException {
        sses.setCallCount(10);
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.stopClock();
    }

    @Test
    public void stopClock_regular() throws ClockException {
        sses.setCallCount(1);
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        clock.stopClock();

        Assert.assertFalse(clock.state());
    }

    @Test(expectedExceptions = TicksTooFastException.class)
    public void testStopClock_Timeout() throws ClockException {
        sses.setCallCount(1);
        Clock clock = new ClockImpl(
                set -> new ShutDownThrowHandler(new TimeoutException()),
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        try {
            clock.stopClock();
        } catch (TicksTooFastException e) {
            Assert.assertFalse(clock.state());
            throw e;
        }
    }

    @Test
    public void testStopClock_Interrupted() throws ClockException {
        sses.setCallCount(1);
        Clock clock = new ClockImpl(
                set -> new ShutDownThrowHandler(new InterruptedException()),
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        clock.stopClock();

        Assert.assertFalse(clock.state());
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void startClock_startTwice() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        clock.startClock();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addClockListenerCL_stoppedClockNull() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.addClockListener(null);
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void addClockListenerCL_startedClock() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        clock.addClockListener(() -> { });
    }

    @Test
    public void addClockListenerCL_stoppedClock_throwsNothing() {
        Clock clock = new ClockImpl(nopFactory,
                sses, 10L, TimeUnit.SECONDS);

        clock.addClockListener(() -> { });
    }

    @Test
    public void wrapToClockException_ClockException() {
        ClockException clockExcep = new ClockException();
        ExecutionException execExcep = new ExecutionException(clockExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);

        Assert.assertSame(wrapped, clockExcep);
    }

    @Test
    public void wrapToClockException_ClockRuntimeException() {
        ClockException clockExcep = new ClockException();
        ClockRuntimeException clockRunExcep = new ClockRuntimeException(clockExcep);
        ExecutionException execExcep = new ExecutionException(clockRunExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);

        Assert.assertSame(wrapped, clockExcep);
    }

    @Test
    public void wrapToClockException_RuntimeException() {
        RuntimeException runExcep = new NullPointerException();
        ExecutionException execExcep = new ExecutionException(runExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);
        Throwable cause = wrapped.getCause();

        Assert.assertSame(cause, runExcep);
    }

    @Test
    public void wrapToClockException_CheckedException() {
        Exception checkedExcep = new CloneNotSupportedException();
        ExecutionException execExcep = new ExecutionException(checkedExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);
        Throwable cause = wrapped.getCause();

        Assert.assertSame(cause, checkedExcep);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
