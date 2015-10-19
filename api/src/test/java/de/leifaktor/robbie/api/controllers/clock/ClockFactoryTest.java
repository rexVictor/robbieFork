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
import de.leifaktor.robbie.api.controllers.clock.ClockTest.ClockListenerMock;
import de.leifaktor.robbie.api.controllers.clock.ClockTest.ClockRestorerMock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Tests ClockFactory implementations.
 */
public class ClockFactoryTest {

    /**
     * Creates new ClockFactory instances.
     */
    @FunctionalInterface
    public interface ClockFactoryFactory {

        /**
         * Creates a new ClockFactory.
         *
         * @return a freshly constructed ClockFactory
         */
        ClockFactory newInstance();

    }

    /**
     * The ClockFactoryFactory providing ClockFactories.
     */
    private final ClockFactoryFactory clockFactoryFactory;

    /**
     * The ClockFactory instance to test.
     */
    private ClockFactory factory;

    /**
     * A ClockRestorerMock instance used for testing.
     */
    private ClockRestorerMock clockRestorer;

    /**
     * A ClockListenerMock instance used for testing.
     */
    private ClockListenerMock clockListener;

    /**
     * Creates a new TestClass instance with the specified factory.
     * @param clockFactoryFactory the ClockFactoryFactory providing
     *                            instances for this test
     */
    public ClockFactoryTest(ClockFactoryFactory clockFactoryFactory) {
        this.clockFactoryFactory = clockFactoryFactory;
    }

    /**
     * Initializes the instance parameters before every test.
     */
    @BeforeMethod
    public void initializeInstanceParameters() {
        clockRestorer = new ClockRestorerMock();
        clockListener = new ClockListenerMock();
        factory = clockFactoryFactory.newInstance();
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            dataProvider = "nonPositiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void new_NegativeTickDuration(long negativeLong, TimeUnit unit) {
        factory.newInstance(negativeLong, unit, clockListener, clockRestorer);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongs",
            dataProviderClass = ClockTest.class)
    public void new_nullTimeUnit(long duration) {
        factory.newInstance(duration, null, clockListener, clockRestorer);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void new_nullListener(long duration, TimeUnit unit) {
        factory.newInstance(duration, unit, null, clockRestorer);
    }

    @Test(expectedExceptions = NullPointerException.class,
            dataProvider = "positiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void new_nullRestorer(long duration, TimeUnit unit) {
        factory.newInstance(duration, unit, clockListener, null);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void getTickDuration(long duration, TimeUnit unit) {
        Clock freshClock = factory.newInstance(
                duration, unit, clockListener, clockRestorer);

        Assert.assertEquals(freshClock.getTickDuration(unit), duration);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void getClockListener(long duration, TimeUnit unit) {
        Clock freshClock = factory.newInstance(
                duration, unit, clockListener, clockRestorer);

        Assert.assertSame(freshClock.getClockListener(), clockListener);
    }

    @Test(dataProvider = "positiveLongsCrossTimeUnits",
            dataProviderClass = ClockTest.class)
    public void getClockRestorer(long duration, TimeUnit unit) {
        Clock freshClock = factory.newInstance(
                duration, unit, clockListener, clockRestorer);

        Assert.assertSame(freshClock.getClockRestorer(), clockRestorer);
    }

}
