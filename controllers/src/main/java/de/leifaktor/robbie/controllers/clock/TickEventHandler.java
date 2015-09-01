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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This defines how tickEvents should be handled.
 */
public interface TickEventHandler {

    /**
     * Runs the listeners if the current thread is not interrupted,
     * and does nothing otherwise.
     */
    void run();

    /**
     * Properly shutsdown this runnable, if it can.
     *
     * <p>Otherwise it forces shutdown and throws the causing exceptions.
     *
     * @param timeOutDuration how long to wait for remaining threads to finish
     * @param timeOutUnit the unit of timeOutDuration
     * @return true if all tasks could finish regularly, false otherwise
     * @throws InterruptedException if the calling thread gets interrupted.
     * @throws TimeoutException if executorService could not be shutdown in time
     */
    boolean shutdown(long timeOutDuration, TimeUnit timeOutUnit)
                throws TimeoutException, InterruptedException;

    /**
     * Returns if all previous began tasks have been finished.
     *
     * @return if all previous began tasks have been finished
     */
    boolean areDone();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
