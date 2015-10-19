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

import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import de.leifaktor.robbie.api.controllers.clock.Clock;
import de.leifaktor.robbie.api.controllers.clock.ClockListener;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implements the Clock interface thread-safe.
 */
public class ClockImpl implements Clock {

    /**
     * The Logger for this clock implementation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClockImpl.class);

    private final ClockListener clockListener;

    /**
     * The ExecutorService for the ticks.
     */
    private final ListeningScheduledExecutorService executor;

    private final ClockRestorer restorer;

    private ClockState clockState = ClockState.STOPPED;

    /**
     * The duration of a tick.
     */
    private long tickDuration;

    /**
     * The unit of the duration of a tick.
     */
    private TimeUnit tickDurationUnit;

    /**
     * The current periodic fireTickEvent caller.
     */
    private ListenableScheduledFuture<?> callerHandle;

    /**
     * Creates a new Clock.
     *
     * @param executor the scheduledExecutorService to schedule this Clock in.
     *                It may get shutdown.
     * @param tickDuration the duration of a tick
     * @param tickDurationUnit the unit of tickDuration
     * @param restorer
     * @throws NullPointerException if tickDurationUnit is null
     * @throws IllegalArgumentException if tickDuration is not positive
     */
    public ClockImpl(ScheduledExecutorService executor,
                     long tickDuration, TimeUnit tickDurationUnit,
                     ClockRestorer restorer, ClockListener clockListener) {
        this.restorer = Objects.requireNonNull(restorer);
        this.executor = MoreExecutors.listeningDecorator(executor);
        this.clockListener = Objects.requireNonNull(clockListener);
        setTickDuration0(tickDuration, tickDurationUnit);
    }

    private void throwExceptionIfShutdown() {
        if (clockState == ClockState.SHUTDOWN) {
            throw new IllegalStateException("Clock has been shutdown.");
        }
    }

    @Override
    public void setTickDuration(long duration, TimeUnit timeUnit) {
        throwExceptionIfShutdown();
        if (clockState != ClockState.PAUSED && clockState != ClockState.STOPPED) {
            throw new IllegalStateException("Clock is running.");
        }
        setTickDuration0(duration, timeUnit);
    }

    @Override
    public void shutdown() {
        throwExceptionIfShutdown();
        if (clockState != ClockState.STOPPED) {
            throw new IllegalStateException("Clock is not stopped.");
        }
        executor.shutdown();
        try {
            executor.awaitTermination(tickDuration, tickDurationUnit);
        } catch (InterruptedException e) {
            LOGGER.debug("Got interrupted during shutdown.", e);
        }
        finally {
            executor.shutdownNow();
        }
        clockState = ClockState.SHUTDOWN;
    }

    @Override
    public void startClock() {
        throwExceptionIfShutdown();
        if (clockState != ClockState.STOPPED) {
            throw new IllegalStateException("Clock is not stopped.");
        }
        clockState = ClockState.PAUSED;
        resumeClock();
    }

    @Override
    public void stopClock() {
        throwExceptionIfShutdown();
        if (clockState == ClockState.STOPPED) {
            throw new IllegalStateException("Clock is already stopped");
        }
        if (clockState == ClockState.RUNNING) {
            pauseClock();
        }
        //Clock is guaranteed paused.
        clockState = ClockState.STOPPED;
    }

    @Override
    public void pauseClock() {
        throwExceptionIfShutdown();
        if (clockState != ClockState.RUNNING) {
            throw new IllegalStateException("Clock is not running.");
        }
        callerHandle.cancel(false);
        clockState = ClockState.PAUSED;
    }

    @Override
    public void resumeClock() {
        throwExceptionIfShutdown();
        if (clockState != ClockState.PAUSED) {
            throw new IllegalStateException("Clock is not paused.");
        }
        callerHandle = executor.scheduleAtFixedRate(clockListener::ticksPassed, 0,
                tickDuration, tickDurationUnit);
        ClockCallBack.addCallback(
                callerHandle, throwable -> restorer.exceptionHappened(this, throwable),
                executor);
        clockState = ClockState.RUNNING;
    }


    @Override
    public ClockState state() {
        return clockState;
    }

    @Override
    public long getTickDuration(TimeUnit unit) {
        return unit.convert(tickDuration, tickDurationUnit);
    }

    @Override
    public ClockListener getClockListener() {
        return clockListener;
    }

    @Override
    public ClockRestorer getClockRestorer() {
        return restorer;
    }

    /**
     * Sets the tick duration and unit for the given TickEventHandler.
     *
     * @param duration the duration to set
     * @param unit the unit to set
     * @throws NullPointerException if tickEventHandler or unit is null
     * @throws IllegalArgumentException if duration is negative
     */
    private void setTickDuration0(
            long duration, TimeUnit unit) {
        if (duration < 0) {
            throw new IllegalArgumentException("The duration of a tick may not be negative.");
        }
        tickDurationUnit = Objects.requireNonNull(unit);
        tickDuration = duration;
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
