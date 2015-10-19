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

package de.leifaktor.robbie.api.controllers.clock;

import de.leifaktor.robbie.api.controllers.clock.Clock.ClockFactory;
import de.leifaktor.robbie.api.controllers.clock.Clock.ClockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rex.palace.testhelp.ArgumentConverter;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Tests implementations of the Clock interface.
 */
public class ClockTest {

    /**
     * The Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClockTest.class);

    /**
     * A mock implementation of ClockListener used for testing.
     */
    static class ClockListenerMock implements ClockListener {

        /**
         * The Exception to throw when ticksPassed() is called.
         */
        public RuntimeException exception;

        /**
         * The number of times ticksPassed() has been called.
         */
        public int callCount = 0;

        /**
         * Creates a new ClockListenerMock.
         */
        ClockListenerMock() {
            super();
        }

        @Override
        public void ticksPassed() {
            if (exception != null) {
                throw exception;
            }
            callCount++;
        }
    }

    /**
     * A mock implementation of ClockRestorer.
     */
    static class ClockRestorerMock implements ClockRestorer {

        /**
         * The brokenClock in which an Exception occurred.
         */
        public Clock brokenClock;

        /**
         * The occurred Exception.
         */
        public Throwable throwable;

        /**
         * Creates a new ClockRestorerMock.
         */
        ClockRestorerMock() {
            super();
        }

        @Override
        public void exceptionHappened(Clock clock, Throwable exception) {
            brokenClock = clock;
            throwable = exception;
        }
    }

    /**
     * The ClockFactory providing Clock instances.
     */
    private final ClockFactory factory;

    /**
     * A ClockRestorerMock instance used for testing.
     */
    private ClockRestorerMock clockRestorer;

    /**
     * A ClockListenerMock instance used for testing.
     */
    private ClockListenerMock clockListener;

    /**
     * A fresh clock used for testing.
     */
    private Clock clock;

    /**
     * Creates a new TestClass instance with the specified factory.
     * @param factory the ClockFactory providing instances for this test
     */
    public ClockTest(ClockFactory factory) {
        this.factory = factory;
    }

    @DataProvider(name = "positiveLongs")
    public static Iterator<Object[]> getPositiveLongs() {
        Stream<Long> stream = LongStream.range(1L, 11L).boxed();
        return ArgumentConverter.convert(stream);
    }

    @DataProvider(name = "nonPositiveLongsCrossTimeUnits")
    public static Iterator<Object[]> getNonPositiveLongsCrossTimeUnits() {
        Object[] longs = LongStream.range(-9L, 0L).boxed().toArray();
        TimeUnit[] units = TimeUnit.values();
        return ArgumentConverter.cross(longs, units);
    }

    @DataProvider(name = "positiveLongsCrossTimeUnits")
    public static Iterator<Object[]> getPositiveLongsCrossTimeUnits() {
        Object[] longs = LongStream.range(1L, 11L).boxed().toArray();
        TimeUnit[] units = TimeUnit.values();
        return ArgumentConverter.cross(longs, units);
    }

    /**
     * Initializes the instance fields.
     */
    @BeforeMethod
    public void initializeInstanceFields() {
        clockRestorer = new ClockRestorerMock();
        clockListener = new ClockListenerMock();
        clock = factory.newInstance(10L, TimeUnit.MILLISECONDS,
                clockListener, clockRestorer);
    }

    /**
     * Shuts the clock down.
     */
    @AfterMethod
    public void shutdownClock() {
        try {
            clock.shutdown();
        } catch (Exception e) {
            //ignore
        }
    }

    @Test
    public void state_afterConstruction_stopped() {
        Assert.assertSame(clock.state(), ClockState.STOPPED);
    }

    //Illegal state transitions

    @Test(expectedExceptions = IllegalStateException.class)
    public void pauseClock_stopped() {
        clock.pauseClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void stopClock_stopped() {
        clock.stopClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shutdown_running() {
        clock.startClock();

        clock.shutdown();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void resumeClock_running() {
        clock.startClock();

        clock.resumeClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void startClock_running() {
        clock.startClock();

        clock.startClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void pauseClock_paused() {
        clock.startClock();
        clock.pauseClock();

        clock.pauseClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void shutdown_paused() {
        clock.startClock();
        clock.pauseClock();

        clock.shutdown();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void startClock_shutdown() {
        clock.shutdown();

        clock.startClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void pauseClock_shutdown() {
        clock.shutdown();

        clock.pauseClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void resumeClock_shutdown() {
        clock.shutdown();

        clock.resumeClock();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void stopClock_shutdown() {
        clock.shutdown();

        clock.stopClock();
    }

    //Illegal calls to startClock()

    @Test(expectedExceptions = IllegalStateException.class)
    public void startClock_paused() {
        clock.startClock();
        clock.pauseClock();

        clock.startClock();
    }

    // Legal transitions

    @Test
    public void shutdown_stopped() {
        clock.shutdown();

        Assert.assertSame(clock.state(), ClockState.SHUTDOWN);
    }

    @Test
    public void startClock_stopped() {
        clock.startClock();

        Assert.assertSame(clock.state(), ClockState.RUNNING);
    }

    @Test
    public void pauseClock_running() {
        clock.startClock();

        clock.pauseClock();

        Assert.assertSame(clock.state(), ClockState.PAUSED);
    }

    @Test
    public void stopClock_running() {
        clock.startClock();

        clock.stopClock();

        Assert.assertSame(clock.state(), ClockState.STOPPED);
    }

    @Test
    public void resumeClock_paused() {
        clock.startClock();
        clock.pauseClock();

        clock.resumeClock();

        Assert.assertSame(clock.state(), ClockState.RUNNING);
    }

    @Test
    public void stopClock_paused() {
        clock.startClock();
        clock.pauseClock();

        clock.stopClock();

        Assert.assertSame(clock.state(), ClockState.STOPPED);
    }

    // Assert ShutdownException is thrown
    @Test(expectedExceptions = IllegalStateException.class,
            dataProvider = "positiveLongsCrossTimeUnits")
    public void setTickDuration_shutdown(long duration, TimeUnit unit) {
        clock.shutdown();

        clock.setTickDuration(duration, unit);
    }

    // IllegalArgumentException, NullPointerException tests

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongs")
    public void setTickDuration_nullUnit(long duration) {
        clock.setTickDuration(duration, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "nonPositiveLongsCrossTimeUnits")
    public void setTickDuration_nonPositiveDuration(long duration, TimeUnit timeUnit) {
        clock.setTickDuration(duration, timeUnit);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits")
    public void setTickDuration_equals_getDuration(long duration, TimeUnit unit) {
        clock.setTickDuration(duration, unit);
        long actualDuration = clock.getTickDuration(unit);

        Assert.assertEquals(actualDuration, duration);
    }

    //Tests with mock objects

    @Test
    public void listenerCalled() throws InterruptedException {
        clock.setTickDuration(10L, TimeUnit.MILLISECONDS);
        clock.startClock();
        TimeUnit.MILLISECONDS.sleep(55L);

        Assert.assertNull(clockRestorer.throwable);
        Assert.assertTrue(clockListener.callCount > 0);
        if (clockListener.callCount != 5) {
            LOGGER.warn("CallCount was {}, but should have been {}.",
                    clockListener.callCount, 5);
        }

    }

    @Test
    public void restorerCalledWhenException()
            throws InterruptedException {
        clock.setTickDuration(10L, TimeUnit.MILLISECONDS);
        clockListener.exception = new ClassCastException();
        clock.startClock();

        while (clockRestorer.throwable == null) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }

        Assert.assertSame(clockRestorer.throwable, clockListener.exception);
    }

}