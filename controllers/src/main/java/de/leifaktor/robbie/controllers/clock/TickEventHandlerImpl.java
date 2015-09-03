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

import de.leifaktor.robbie.api.controllers.clock.ClockListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Notifies the clock listeners.
 */
public class TickEventHandlerImpl implements TickEventHandler {

    /**
     * The tasks to run on tick.
     */
    private final Set<Runnable> listenerTasks;

    /**
     * The ExecutorService which runs the listeners.
     */
    private final ExecutorService executorService;

    /**
     * The currently running or if none previous ran tasks.
     */
    private List<Future<?>> runningTasks = Collections.emptyList();

    /**
     * Creates a new TickEventHandler.
     * @param executorService executorService to run the ClockListeners.
     *        It may be shutdown during execution.
     * @param listeners the list of ClockListeners to be called.
     */
    public TickEventHandlerImpl(ExecutorService executorService,
            Collection<? extends ClockListener> listeners) {
        this.executorService = Objects.requireNonNull(executorService);
        listenerTasks = listeners.stream().map(this::convert).collect(Collectors.toSet());
    }

    /**
     * Converts a ClockListener to a Runnable.
     *
     * @param cl the clockListener to convert
     * @return the converted Runnable
     */
    private Runnable convert(ClockListener cl) {
        return cl::ticksPassed;
    }


    @Override
    public void run() {
        if (!Thread.currentThread().isInterrupted()) {
            runningTasks = new ArrayList<>(listenerTasks.size());
            listenerTasks.stream().forEach(this::runListener);
        }
    }

    @Override
    public boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                throws TimeoutException, InterruptedException {
        executorService.shutdown();
        boolean correctTerminated = false;
        try {
            if (executorService.awaitTermination(timeOutDuration, timeOutUnit)) {
                correctTerminated = true;
            } else {
                throw new TimeoutException("Tasks did not terminate in time.");
            }
        } finally {
            executorService.shutdownNow();
            runningTasks = null;
        }
        return correctTerminated;
    }

    /**
     * Actually calls the listeners.
     * @param listener the Runnable-wrapped listener to call
     */
    private void runListener(Runnable listener) {
        Future<?> future = executorService.submit(listener);
        runningTasks.add(future);
    }

    @Override
    public boolean areDone() {
        return runningTasks.stream().allMatch(Future::isDone);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
