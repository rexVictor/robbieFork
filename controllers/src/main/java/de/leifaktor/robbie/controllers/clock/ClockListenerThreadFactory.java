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

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A ThreadFactory implementation providing Threads for ClockListener tasks.
 *
 * <p>All Threads created by this are daemon threads and have an
 * UncaughtExceptionHandler.
 *
 * <p>The names of the Threads are unique and follow the pattern:
 * ClockListenerThread-[number]-[number]-[Runnable.toString()],
 * where [number] means any integer decimal number and the last part
 * is obtained by calling the toString() method on the Runnable object,
 * for which a Thread shall be created.
 */
public class ClockListenerThreadFactory implements ThreadFactory{

    /**
     * Counter for the times this class got instantiated.
     */
    private static AtomicInteger instanceCount = new AtomicInteger(0);

    /**
     * The current number of this instance.
     */
    private final int currentInstance;

    /**
     * The number of threads this task submitted.
     */
    private final AtomicInteger threadCount = new AtomicInteger(0);

    /**
     * The UncaughtExceptionHandler to use for Threads.
     */
    private final Thread.UncaughtExceptionHandler handler;

    /**
     * Creates a new ClockListenerThreadFactory.
     *
     * @param handler the UncaughtExceptionHandler to attach to the threads
     * @throws NullPointerException if handler is null
     */
    public ClockListenerThreadFactory(Thread.UncaughtExceptionHandler handler) {
        this(handler, instanceCount.getAndIncrement());
    }

    /**
     * A constructor intended to be used for testing purposes.
     *
     * <p>Using this constructor does not guarantee the uniqueness of Thread
     * names.
     *
     * @param handler the UncaughtExceptionHandler to attach to the threads
     * @param instanceCount the "simulated" instance number
     * @throws NullPointerException if handler is null
     */
    protected ClockListenerThreadFactory(
            Thread.UncaughtExceptionHandler handler, int instanceCount) {
        this.handler = Objects.requireNonNull(handler);
        currentInstance = instanceCount;
    }

    /**
     * Creates a new daemon Thread with a unique name
     * and the UncaughtExceptionHandler set when instantiating this class.
     *
     * @param task the task the returned Thread shall run
     * @return a daemon Thread running task when started
     * @throws NullPointerException if task is null
     * @see ClockListenerThreadFactory
     */
    @Override
    public Thread newThread(Runnable task) {
        Thread thread = new Thread(Objects.requireNonNull(task));
        thread.setName(makeName(task));
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }

    /**
     * Creates a name according to the class doc.
     *
     * @param runnable the task being attached to a Thread
     * @return a String according to the class doc
     * @throws NullPointerException if runnable is null
     * @see ClockListenerThreadFactory
     */
    private String makeName(Runnable runnable) {
        return String.format("ClockListenerThread-%1$d-%2$d-%3$s",
                currentInstance, threadCount.getAndIncrement(), runnable);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
