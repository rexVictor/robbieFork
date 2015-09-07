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

import java.util.concurrent.TimeUnit;

/**
 * A Clock is the time measurement ingame.
 *
 * <p>The smallest time unit is a tick.
 *
 * <p>Usually there should be only one Clock instance per Episode.
 *
 * <p>Note: Implementations must be thread-safe.
 */
public interface Clock {

    /**
     * Adds a ClockListener to this clock.
     *
     * <p>ClockListeners added via this method will be called every tick.
     *
     * @param listener the ClockListener to be registered
     * @throws NullPointerException if listener is null
     */
    void addClockListener(ClockListener listener);

    /**
     * Sets the duration of a tick.
     *
     * @param duration the duration of a tick
     * @param timeUnit the unit of duration
     * @throws NullPointerException if timeUnit is null
     * @throws IllegalArgumentException if duration is not positive
     */
    void setTickDuration(long duration, TimeUnit timeUnit);

    /**
     * Starts the clock.
     */
    void startClock();

    /**
     * Stops the clock.
     *
     * @throws ClockException if something went wrong; however
     *         after calling this method this clock must have stopped
     *         and be able to start again with a call to startClock()
     */
    void stopClock() throws ClockException;

    /**
     * Returns the state of the clock.
     *
     * <p>A clock can either be started or stopped.
     *
     * <p>This either returns the state or throws the exception causing
     * the clock to stop.
     *
     * @return true if this clock is started or false if it is stopped
     * @throws ClockException if the clock stopped because of an exception
     */
    boolean state() throws ClockException;

    /**
     * Returns the current tick duration in unit.
     *
     * @param unit the unit in which to return the duration
     * @return the current tick duration in unit
     */
    long getTickDuration(TimeUnit unit);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
