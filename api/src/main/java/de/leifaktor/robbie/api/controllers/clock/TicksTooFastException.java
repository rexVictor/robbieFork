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

import de.leifaktor.robbie.api.controllers.clock.ClockException;

/**
 * Indicates that one tick is too fast.
 *
 * <p>It is thrown whenever another tick happens, before all
 * ClockListeners are done handling the previous tick.
 */
public class TicksTooFastException extends ClockException {

    /**
     * Needed for possible serialization.
     */
    private static final long serialVersionUID = 0x71cb6f906b139497L;

    /**
     * Constructs a new TicksTooFastException with null as its default message.
     */
    public TicksTooFastException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public TicksTooFastException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause the cause. Null is allowed.
     */
    public TicksTooFastException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of (cause==null ? null : cause.toString()).
     *
     * @param cause the cause. Null is allowed.
     */
    public TicksTooFastException(Throwable cause) {
        super(cause);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
