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

import de.leifaktor.robbie.api.controllers.Clock;
import de.leifaktor.robbie.api.controllers.ClockException;

import org.testng.Assert;
import org.testng.annotations.Test;

import rex.palace.testes.SequentialScheduledExecutorService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Tests the Clock impementation.
 */
public class ClockTest {

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
        public CallCounter() {
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
     * Default Constructor.
     */
    public ClockTest() {
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void new_NegativeTickDuration() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        TickEventHandlerFactory factory = set -> null;
        Clock clock = new ClockImpl(factory,
                sses, -1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullExecutorService() {
        TickEventHandlerFactory factory = set -> null;
        Clock clock = new ClockImpl(factory,
                null, 1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullTickEventHandlerFactory() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        Clock clock = new ClockImpl(null,
                sses, 1L, TimeUnit.MILLISECONDS);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullTimeUnit() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        TickEventHandlerFactory factory = set -> null;
        Clock clock = new ClockImpl(factory,
                sses, 1L, null);
    }

    @Test
    public void getTickDurationInMillis() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> null;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        Assert.assertEquals(clock.getTickDurationinMillis(), 10L);
    }

    @Test
    public void setTickDuration_StoppedClock() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> null;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(5L, TimeUnit.SECONDS);

        Assert.assertEquals(clock.getTickDurationinMillis(), 5000L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setTickDuration_NegativeDuration_StoppedClock() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(-1L, TimeUnit.MILLISECONDS);

    }

    @Test(expectedExceptions = NullPointerException.class)
    public void setTickDuration_NullUnit_StoppedClock() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(1L, null);

    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void setTickDuration_StartedClock() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        clock.setTickDuration(5L, TimeUnit.SECONDS);

    }

    @Test
    public void startClock_noExceptions() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        sses.setCallCount(10);
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        Assert.assertEquals(callCounter.count, 10);

    }

    @Test(expectedExceptions = ClockAlreadyStoppedException.class)
    public void stopClock_neverStarted() throws ClockException {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        sses.setCallCount(10);
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.stopClock();
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void startClock_startTwice() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        sses.setCallCount(10);
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        clock.startClock();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addClockListenerCL_stoppedClockNull() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.addClockListener(null);

    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void addClockListenerCL_startedClock() {
        SequentialScheduledExecutorService sses
                = new SequentialScheduledExecutorService();
        CallCounter callCounter = new CallCounter();
        TickEventHandlerFactory factory = set -> callCounter;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

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
