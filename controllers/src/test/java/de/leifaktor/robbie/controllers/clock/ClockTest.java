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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import rex.palace.testes.scheduled.SequentialScheduledExecutorService;
import rex.palace.testes.scheduled.TimeController;
import rex.palace.testes.scheduled.TimeControllerImpl;
import rex.palace.testhelp.ArgumentConverter;
import rex.palace.testhelp.TestThread;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;


/**
 * Tests the Clock implementation.
 */
public class ClockTest {

    /**
     * The TimeUnits used for testing.
     */
    private static final Object[] VALID_TIME_UNITS =
            { TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS };

    /**
     * The limit of stream elements for testing.
     */
    private static final int MAX_RUNS = 5;


    /**
     * A Mock implementation of TickEventHandler.
     */
    private static class TickEventHandlerMock implements TickEventHandler {

        /**
         * The return vale of shutdown.
         */
        public boolean shutdown = false;

        /**
         * The return value of areDone.
         */
        public boolean areDone = true;

        /**
         * The exception shutdown throws.
         */
        public Exception exception = null;

        /**
         * The number of times run() got called.
         */
        public int count = 0;


        /**
         * Creates a new TickEventHandlerMock.
         *
         */
        TickEventHandlerMock() {
        }

        @Override
        public void run() {
            count++;
        }

        @Override
        public boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                    throws TimeoutException, InterruptedException {
            if (exception == null) {
                return shutdown;
            }
            try {
                throw exception;
            } catch (TimeoutException | InterruptedException | RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean areDone() {
            return areDone;
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
     * The TickEventHandlerMock tests can use.
     */
    private TickEventHandlerMock tickEventHandlerMock;

    /**
     * A Nop factory.
     */
    private final TickEventHandlerFactory mockFactory = set -> tickEventHandlerMock;

    /**
     * The TimeController these tests use.
     */
    private TimeController timeController;

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
        timeController = new TimeControllerImpl();
        sses = new SequentialScheduledExecutorService(timeController);
        tickEventHandlerMock = new TickEventHandlerMock();
    }

    @DataProvider(name = "negativeLongsCrossTimeUnits")
    public Iterator<Object[]> getNegativeLongsCrossTimeUnits() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1, MAX_RUNS).mapToObj(lg -> -lg).toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "positiveLongsCrossTimeUnits")
    public Iterator<Object[]> getPositiveLongsCrossTimeUnits() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1, MAX_RUNS).mapToObj(Long::valueOf).toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "randomNegativeLongsCrossTimeUnits")
    public Iterator<Object[]> getRandomNegativeLongs() {
        Random random = new Random();
        LongStream stream = random.longs(MAX_RUNS, Long.MIN_VALUE, 0L);
        return ArgumentConverter.cross(stream.mapToObj(Long::valueOf).toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "randomPositiveLongsCrossTimeUnits")
    public Iterator<Object[]> getRandomPositiveLongsCrossTimeUnits() {
        Random random = new Random();
        LongStream stream = random.longs(MAX_RUNS, 1L, Long.MAX_VALUE / 10000);
        return ArgumentConverter.cross(stream.mapToObj(lg -> lg).toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "positiveLongs")
    public Iterator<Object[]> getPositiveLongs() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1, MAX_RUNS).mapToObj(Long::valueOf).toArray());
    }

    @DataProvider(name = "randomPositiveLongs")
    public Iterator<Object[]> getRandomPositiveLongs() {
        Random random = new Random();
        return ArgumentConverter.cross(random.longs(
                MAX_RUNS, 1, Long.MAX_VALUE).mapToObj(Long::valueOf).toArray());
    }

    @DataProvider(name = "positiveLongsCrossTimeUnitsCrossCrossNegativeLongsCrossUnit")
    public Iterator<Object[]> positiveLongs_TimeUnits_NegativeLongs_TimeUnits() {
        Object[] positives = LongStream.range(1L, MAX_RUNS).mapToObj(Long::valueOf).toArray();
        Object[] negatives = Arrays.stream(positives).map(lg -> - (long) lg).toArray();
        return ArgumentConverter.cross(positives, VALID_TIME_UNITS, negatives, VALID_TIME_UNITS);
    }


    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "negativeLongsCrossTimeUnits")
    public void new_NegativeTickDuration(long negativeLong, TimeUnit unit) {
        new ClockImpl(nullFactory, sses, negativeLong, unit);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "randomNegativeLongsCrossTimeUnits")
    public void new_NegativeTickDuration_random(long negativeLong, TimeUnit unit) {
        new_NegativeTickDuration(negativeLong, unit);
    }



    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongsCrossTimeUnits")
    public void new_nullExecutorService(long duration, TimeUnit unit) {
        new ClockImpl(nullFactory, null, duration, unit);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "randomPositiveLongsCrossTimeUnits")
    public void new_nullExecutorServiceRandom(long duration, TimeUnit unit) {
        new_nullExecutorService(duration, unit);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongsCrossTimeUnits")
    public void new_nullTickEventHandlerFactory(long duration, TimeUnit unit) {
        new ClockImpl(null, sses, duration, unit);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "randomPositiveLongsCrossTimeUnits")
    public void new_nullTickEventHandlerFactoryRandom(long duration, TimeUnit unit) {
        new_nullTickEventHandlerFactory(duration, unit);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongs")
    public void new_nullTimeUnit(long duration) {
        new ClockImpl(nullFactory, sses, duration, null);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "randomPositiveLongs")
    public void new_nullTimeUnit_random(long duration) {
        new_nullTimeUnit(duration);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits")
    public void getTickDuration(long duration, TimeUnit unit) {
        Clock clock = new ClockImpl(nullFactory,
                sses, duration, unit);

        Assert.assertEquals(clock.getTickDuration(unit), duration);
    }

    @Test(dataProvider = "randomPositiveLongsCrossTimeUnits")
    public void getTickDuration_random(long duration, TimeUnit unit) {
        getTickDuration(duration, unit);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits")
    public void setTickDuration_StoppedClock(long duration, TimeUnit unit) {
        Clock clock = new ClockImpl(nullFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(duration, unit);

        Assert.assertEquals(clock.getTickDuration(unit), duration);
    }

    @Test(dataProvider = "randomPositiveLongsCrossTimeUnits")
    public void setTickDuration_StoppedClock_random(long duration, TimeUnit unit) {
        setTickDuration_StoppedClock(duration, unit);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "positiveLongsCrossTimeUnitsCrossCrossNegativeLongsCrossUnit")
    public void setTickDuration_NegativeDuration_StoppedClock(
            long initialDuration, TimeUnit initialUnit, long negativeLong, TimeUnit unit) {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(negativeLong, unit);
    }


    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "positiveLongsCrossTimeUnitsCrossCrossNegativeLongsCrossUnit")
    public void setTickDuration_RandomNegativeDuration_StoppedClock(
            long initialDuration, TimeUnit initialUnit, long negativeLong, TimeUnit unit) {
        setTickDuration_NegativeDuration_StoppedClock(
                initialDuration, initialUnit, negativeLong, unit);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void setTickDuration_NullUnit_StoppedClock() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.setTickDuration(1L, null);
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void setTickDuration_StartedClock() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        clock.setTickDuration(5L, TimeUnit.SECONDS);
    }

    @Test
    public void startClock_noExceptions() throws ClockException {
        tickEventHandlerMock = new TickEventHandlerMock();
        TickEventHandlerFactory factory = set -> tickEventHandlerMock;
        Clock clock = new ClockImpl(factory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        timeController.letTimePass(99L, TimeUnit.MILLISECONDS);

        Assert.assertEquals(tickEventHandlerMock.count, 10);
        Assert.assertTrue(clock.state());
    }

    @Test(expectedExceptions = TicksTooFastException.class)
    public void startClock_NotAllDone() throws ClockException {
        tickEventHandlerMock.areDone = false;
        Clock clock = new ClockImpl(mockFactory, sses,
                7L, TimeUnit.NANOSECONDS);

        clock.startClock();

        timeController.letTimePass(7L, TimeUnit.NANOSECONDS);

        clock.state();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void state_interrupted() throws Throwable {
        Clock clock = new ClockImpl(mockFactory, sses,
                7L, TimeUnit.NANOSECONDS);

        clock.startClock();
        timeController.letTimePass(7L, TimeUnit.NANOSECONDS);
        clock.stopClock();

        TestThread thread = new TestThread(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                Thread.currentThread().interrupt();
                clock.state();
                return null;
            }

        });

        thread.start();
        thread.join();
        thread.finish();
    }

    @Test(expectedExceptions = ClockAlreadyStoppedException.class)
    public void stopClock_neverStarted() throws ClockException {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.stopClock();
    }

    @Test
    public void stopClock_regular() throws ClockException {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        timeController.letTimePass(10L, TimeUnit.MILLISECONDS);
        clock.stopClock();

        Assert.assertFalse(clock.state());
    }

    @Test(expectedExceptions = TicksTooFastException.class)
    public void testStopClock_Timeout() throws ClockException {
        tickEventHandlerMock.exception = new TimeoutException();
        Clock clock = new ClockImpl(mockFactory, sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        try {
            timeController.letTimePass(10L, TimeUnit.MILLISECONDS);
            clock.stopClock();
        } catch (TicksTooFastException e) {
            Assert.assertFalse(clock.state());
            throw e;
        }
    }

    @Test
    public void testStopClock_Interrupted() throws ClockException {
        tickEventHandlerMock.exception = new InterruptedException();
        Clock clock = new ClockImpl(mockFactory, sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        timeController.letTimePass(10L, TimeUnit.MILLISECONDS);
        clock.stopClock();

        Assert.assertFalse(clock.state());
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void startClock_startTwice() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();
        clock.startClock();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addClockListenerCL_stoppedClockNull() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.addClockListener(null);
    }

    @Test(expectedExceptions = ClockAlreadyStartedException.class)
    public void addClockListenerCL_startedClock() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.MILLISECONDS);

        clock.startClock();

        clock.addClockListener(() -> { });
    }

    @Test
    public void addClockListenerCL_stoppedClock_throwsNothing() {
        Clock clock = new ClockImpl(mockFactory,
                sses, 10L, TimeUnit.SECONDS);

        clock.addClockListener(() -> { });
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
