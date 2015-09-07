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
import de.leifaktor.robbie.api.controllers.clock.TicksTooFastException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implements the Clock interface thread-safe.
 */
public class ClockImpl implements Clock {

    /**
     * The Logger for this clock implementation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Clock.class);

    /**
     * The ClockListeners called on every tick.
     */
    private final List<ClockListener> listeners = new LinkedList<>();

    /**
     * The duration of a tick.
     */
    private long tickDuration;

    /**
     * The unit of the duration of a tick.
     */
    private TimeUnit tickDurationUnit;

    /**
     * The ExecutorService for the ticks.
     */
    private final ScheduledExecutorService scheduler;

    /**
     * The current periodic fireTickEvent caller.
     */
    private ScheduledFuture<?> callerHandle;

    /**
     * The TickEventHandlerFactory to be used.
     */
    private final TickEventHandlerFactory tickEventHandlerFactory;

    /**
     * The current TickEventHandler.
     */
    private TickEventHandler tickEventHandler;

    /**
     * Creates a new Clock.
     *
     * @param factory the TickEventHandlerFactory to be used
     * @param scExSer the scheduledExecutorService to schedule this Clock in.
     *                It may get shutdown.
     * @param tickDuration the duration of a tick
     * @param tickDurationUnit the unit of tickDuration
     * @throws NullPointerException if tickDurationUnit is null
     * @throws IllegalArgumentException if tickDuration is not positive
     */
    public ClockImpl(TickEventHandlerFactory factory,
            ScheduledExecutorService scExSer,
            long tickDuration, TimeUnit tickDurationUnit) {
        this.tickEventHandlerFactory = Objects.requireNonNull(factory);
        this.scheduler = Objects.requireNonNull(scExSer);
        ClockImpl.setTickDuration0(this, tickDuration, tickDurationUnit);
    }

    @Override
    public void setTickDuration(long duration, TimeUnit timeUnit) {
        if (tickEventHandler != null) {
            throw new ClockAlreadyStartedException();
        }
        ClockImpl.setTickDuration0(this, duration, timeUnit);
    }

    @Override
    public void addClockListener(ClockListener listener) {
        if (tickEventHandler != null) {
            throw new ClockAlreadyStartedException();
        }
        listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void startClock() {
        if (tickEventHandler != null) {
            throw new ClockAlreadyStartedException();
        }
        tickEventHandler = tickEventHandlerFactory.create(listeners);
        callerHandle = scheduler.scheduleAtFixedRate(this::fireEvents, 0,
                tickDuration, tickDurationUnit);
    }

    /**
     * Fires the events.
     */
    private void fireEvents() {
        if (!tickEventHandler.areDone()) {
            ClockException cause = new TicksTooFastException(
                    "Tasks of previous tick haven't finished.");
            throw new ClockRuntimeException(cause);
        }
        tickEventHandler.run();
    }

    @Override
    public void stopClock() throws ClockException {
        if (tickEventHandler == null) {
            throw new ClockAlreadyStoppedException();
        }
        callerHandle.cancel(false);
        try {
            tickEventHandler.shutdown(tickDuration, tickDurationUnit);
        } catch (TimeoutException e) {
            LOGGER.debug("Got TimeoutException.", e);
            throw new TicksTooFastException("Tasks didn't stop in time.");
        } catch (InterruptedException e) {
            LOGGER.error("Got interrupted stopping the clock.", e);
        } finally {
            tickEventHandler = null;
        }
    }


    @Override
    public boolean state() throws ClockException {
        if (!callerHandle.isDone()) {
            return true;
        }
        try {
            callerHandle.get();
            return false;
        } catch (InterruptedException e) {
            LOGGER.error("This can't happen, since callerHandle is done.", e);
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            LOGGER.debug("Execution Exception in clock.", e);
            throw ClockImpl.wrapToClockException(e);
        }
    }

    @Override
    public long getTickDuration(TimeUnit unit) {
        return unit.convert(tickDuration, tickDurationUnit);
    }

    /**
     * Wraps the exception in a ClockException.
     *
     * <p>If the cause of the exception is of type ClockRuntimeException it returns the causing
     * ClockException. If the cause of the exception is of type ClockException it simply returns
     * it. Otherwise it constructs a new ClockException with the cause as cause.
     *
     * @param execExcep the executionException to process
     * @return a ClockException derived from execExcep
     */
    public static ClockException wrapToClockException(ExecutionException execExcep) {
        Throwable cause = execExcep.getCause();
        if (cause instanceof ClockRuntimeException) {
            cause = cause.getCause();
        }
        if (cause instanceof ClockException) {
            return (ClockException) cause;
        }
        return new ClockException(cause);
    }

    /**
     * Sets the tick duration and unit for the given TickEventHandler.
     *
     * @param clock the Clock to set the duration
     * @param duration the duration to set
     * @param unit the unit to set
     * @throws NullPointerException if tickEventHandler or unit is null
     * @throws IllegalArgumentException if duration is negative
     */
    private static void setTickDuration0(
            ClockImpl clock,
            long duration, TimeUnit unit) {
        if (duration < 0) {
            throw new IllegalArgumentException("The duration of a tick may not be negative.");
        }
        clock.tickDurationUnit = Objects.requireNonNull(unit);
        clock.tickDuration = duration;
    }
}


/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
