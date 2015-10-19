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

/**
 * This describes how to handle Exceptions in a clock.
 *
 * <p>Implementations get called, when an exception in a clock happens.
 */
public interface ClockRestorer {

    /**
     * Gets called, when an exception in the clock occurred.
     *
     * <p>This method must stop the clock and restart it, after
     * handling the exception or rethrow the exception.
     *
     * <p>Note: This method can be run in its own thread.
     *
     * @param clock the clock which threw the exception
     * @param exception the occurred Exception
     */
    void exceptionHappened(Clock clock, Throwable exception);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
