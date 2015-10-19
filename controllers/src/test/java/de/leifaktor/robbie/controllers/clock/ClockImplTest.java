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
import de.leifaktor.robbie.api.controllers.clock.Clock.ClockFactory;
import de.leifaktor.robbie.api.controllers.clock.ClockFactoryTest;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;
import de.leifaktor.robbie.api.controllers.clock.ClockTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import rex.palace.sequentialexecutor.SequentialScheduledExecutorService;
import rex.palace.sequentialexecutor.TimeController;
import rex.palace.sequentialexecutor.TimeControllers;
import rex.palace.testhelp.ArgumentConverter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;


/**
 * Tests the Clock implementation.
 */
public class ClockImplTest {

    /**
     * The TimeUnits used for testing.
     */
    private static final Object[] VALID_TIME_UNITS =
            { TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS };

    /**
     * A ClockFactory providing Clocks.
     */
    private static final ClockFactory FACTORY =
            (duration, unit, listener, clockRestorer) -> {
                ScheduledExecutorService executor
                        = Executors.newSingleThreadScheduledExecutor();
                return new ClockImpl(
                        executor, duration, unit, clockRestorer, listener);
            };

    /**
     * The limit of stream elements for testing.
     */
    private static final long MAX_RUNS = 5L;

    /**
     * The SequentialScheduledExecutorService to use.
     */
    private SequentialScheduledExecutorService sses;

    private final ClockRestorer restorer = (clock, ex) -> { };

    /**
     * The TimeController these tests use.
     */
    private TimeController timeController;

    /**
     * Default Constructor.
     */
    public ClockImplTest() {
    }

    /**
     * Returns an Array of test classes to be executed.
     * @return test classes to be executed
     */
    @Factory
    public Object[] createInstances() {
        return new Object[]{
                new ClockTest(FACTORY),
                new ClockFactoryTest(() -> FACTORY)
        };
    }

    /**
     * Sets up instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariables() {
        timeController = TimeControllers.getInstance();
        sses = new SequentialScheduledExecutorService(timeController);
    }

    @DataProvider(name = "negativeLongsCrossTimeUnits")
    public Iterator<Object[]> getNegativeLongsCrossTimeUnits() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1L, MAX_RUNS).mapToObj(lg -> -lg).toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "positiveLongsCrossTimeUnits")
    public Iterator<Object[]> getPositiveLongsCrossTimeUnits() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1L, MAX_RUNS).boxed().toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "randomNegativeLongsCrossTimeUnits")
    public Iterator<Object[]> getRandomNegativeLongs() {
        Random random = new Random();
        LongStream stream = random.longs(MAX_RUNS, Long.MIN_VALUE, 0L);
        return ArgumentConverter.cross(stream.boxed().toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "randomPositiveLongsCrossTimeUnits")
    public Iterator<Object[]> getRandomPositiveLongsCrossTimeUnits() {
        Random random = new Random();
        LongStream stream = random.longs(MAX_RUNS, 1L, Long.MAX_VALUE / 10000L);
        return ArgumentConverter.cross(stream.boxed().toArray(),
                VALID_TIME_UNITS);
    }

    @DataProvider(name = "positiveLongs")
    public Iterator<Object[]> getPositiveLongs() {
        return ArgumentConverter.cross(
                LongStream.rangeClosed(1L, MAX_RUNS).boxed().toArray());
    }

    @DataProvider(name = "randomPositiveLongs")
    public Iterator<Object[]> getRandomPositiveLongs() {
        Random random = new Random();
        return ArgumentConverter.cross(random.longs(
                MAX_RUNS, 1L, Long.MAX_VALUE).mapToObj(Long::valueOf).toArray());
    }

    @DataProvider(name = "positiveLongsCrossTimeUnitsCrossCrossNegativeLongsCrossUnit")
    public Iterator<Object[]> positiveLongs_TimeUnits_NegativeLongs_TimeUnits() {
        Object[] positives = LongStream.range(1L, MAX_RUNS).mapToObj(Long::valueOf).toArray();
        Object[] negatives = Arrays.stream(positives).map(lg -> - (long) lg).toArray();
        return ArgumentConverter.cross(positives, VALID_TIME_UNITS, negatives, VALID_TIME_UNITS);
    }



    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongsCrossTimeUnits")
    public void new_nullExecutorService(long duration, TimeUnit unit) {
        new ClockImpl(null, duration, unit, restorer, null);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "randomPositiveLongsCrossTimeUnits")
    public void new_nullExecutorServiceRandom(long duration, TimeUnit unit) {
        new_nullExecutorService(duration, unit);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void setTickDuration_StartedClock() {
        Clock clock = new ClockImpl(
                sses, 10L, TimeUnit.MILLISECONDS, restorer, () -> {});

        clock.startClock();

        clock.setTickDuration(5L, TimeUnit.SECONDS);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
