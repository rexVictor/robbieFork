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
 * <p>The smallest time unit is a tick. Every tick
 * {@link ClockListener#ticksPassed()} is called on the {@link ClockListener}
 * returned by {@link #getClockListener()}.
 *
 * <p>Note: This interface has no setter for the {@code ClockListener}. The
 * reason is, that a Clock has not much use without a {@code ClockListener} and
 * therefore needs one at construction time. Furthermore this allows
 * implementations to mark their {@code ClockListener} as final.
 *
 * <p>If an Exception occurs during the run of this Clock
 * {@link ClockRestorer#exceptionHappened(Clock, Throwable)} is called on the
 * {@link ClockRestorer} returned by {@link #getClockRestorer()}. This call
 * may be performed in its own Thread. The Exception is not simply thrown
 * because this Clock may run in its own Thread and therefore the Exception
 * might be left unhandled, leaving this Clock in an inconsistent state.
 *
 * <p>Note: As with the {@code ClockListener} this interface has no setter for
 * the {@code ClockRestorer} for the same reasons.
 *
 * <p>A Clock can be in different {@link ClockState}s. The current state
 * is returned by {@link #state()}. After construction a Clock is
 * {@code STOPPED}.
 * The following state transitions are legal:
 * <ul>
 *     <li> {@code STOPPED} to {@code RUNNING} </li>
 *     <li> {@code STOPPED} to {@code SHUTDOWN} </li>
 *     <li> {@code RUNNING} to {@code PAUSED}  </li>
 *     <li> {@code RUNNING} to {@code STOPPED} </li>
 *     <li> {@code PAUSED} to {@code RUNNING} </li>
 *     <li> {@code PAUSED} to {@code STOPPED} </li>
 * </ul>
 * All other transitions are illegal and trying those will result in an
 * {@link IllegalStateException} or a subclass.
 * This implies that once a Clock has been shutdown, there is no coming back.
 *
 * <p>The state of a clock can be changed with the following methods:
 * <ul>
 *     <li> {@link #startClock()} to {@link ClockState#RUNNING},
 *     if it was {@code STOPPED}</li>
 *     <li> {@link #pauseClock()} to {@link ClockState#PAUSED}</li>
 *     <li> {@link #resumeClock()} to {@link ClockState#RUNNING},
 *     if it was {@code PAUSED} </li>
 *     <li> {@link #stopClock()} to {@link ClockState#STOPPED}</li>
 *     <li> {@link #shutdown()} to {@link ClockState#SHUTDOWN}</li>
 * </ul>
 *
 * <p>If a client doesn't need a Clock instance anymore, it is required that
 * {@code shutdown()} is called, otherwise memory leaks may occur.
 *
 * <p>All methods of this Clock except for {@code state()} throw a
 * {@code IllegalStateException} or a subclass, if this Clock has been shutdown.
 *
 * <p>For performance reasons implementations may throw an
 * {@code IllegalStateException}, when calling
 * modifying methods like
 * {@link #setTickDuration(long, TimeUnit)}, if this Clock is not
 * {@code STOPPED}.
 *
 * <p>Note: Implementations must be thread-safe.
 *
 * @see ClockState
 */
public interface Clock {

    /**
     * An interface for building new Clocks.
     */
    @FunctionalInterface
    interface ClockFactory {

        /**
         * Creates a new {@link Clock} instance.
         *
         * @param tickDuration the initial tickDuration
         * @param unit the TimeUnit of tickDuration
         * @param listener the ClockListener to be notified on every tick
         * @param restorer the ClockRestorer to use, when an Exception occurs
         * @return a new Clock instance
         * @throws NullPointerException if unit, listener or restorer is null
         * @throws IllegalArgumentException if tickDuration is not positive
         */
        Clock newInstance(long tickDuration, TimeUnit unit,
                          ClockListener listener, ClockRestorer restorer);

    }

    enum ClockState {

        RUNNING, PAUSED, STOPPED, SHUTDOWN

    }

    /**
     * Starts the clock.
     *
     * <p>Subsequent calls to {@link #state()} will return
     * {@link ClockState#RUNNING}.
     *
     * @throws IllegalStateException if the clock state was already
     *         {@code RUNNING}
     */
    void startClock();

    /**
     * Stops the clock.
     *
     * <p>Subsequent calls to {@link #state()} will return
     * {@link ClockState#STOPPED}.
     *
     * @throws IllegalStateException if the clock state was already
     *         {@code STOPPED}
     */
    void stopClock();

    /**
     * Pauses the clock.
     *
     * <p>Subsequent calls to {@link #state()} will return
     * {@link ClockState#PAUSED}.
     *
     * @throws IllegalStateException if this Clock is {@code STOPPED}
     */
    void pauseClock();

    /**
     * Resumes the clock.
     *
     * <p>Subsequent calls to {@link #state()} will return
     * {@link ClockState#RUNNING}.
     */
    void resumeClock();

    /**
     * Shuts this clock down.
     *
     * <p>Subsequent calls to {@link #state()} will return forever
     * {@link ClockState#SHUTDOWN}.
     *
     * <p>This causes this Clock to free all its resources. After calling
     * this method this Clock instance is no longer usable, which means if
     * a Clock is again needed, a new one must be constructed.
     */
    void shutdown();

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
     * Returns the current tick duration in unit.
     *
     * @param unit the unit in which to return the duration
     * @return the current tick duration in unit; it is always positive
     */
    long getTickDuration(TimeUnit unit);

    /**
     * Returns the {@link ClockListener} {@link ClockListener#ticksPassed()}
     * is called every tick.
     * @return the ClockListener handling the ticks; it is never null
     */
    ClockListener getClockListener();

    /**
     * Returns the {@link ClockRestorer} handling exceptions.
     * @return the ClockRestorer handling Exceptions; it is never null
     */
    ClockRestorer getClockRestorer();

    /**
     * Returns the state of the clock.
     *
     * <p>A clock can be in different states according to Clock.
     *
     * @return the current state of this Clock; it is never null
     */
    ClockState state();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
