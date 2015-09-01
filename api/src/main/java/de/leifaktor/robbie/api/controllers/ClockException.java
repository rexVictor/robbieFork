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

package de.leifaktor.robbie.api.controllers;

/**
 * Thrown whenever a clock doos something wrong.
 */
public class ClockException extends Exception {

    /**
     * Needed for possible serialization.
     */
    private static final long serialVersionUID = 0x95f30013e02690e7L;

    /**
     * Constructs a new ClockException with null as its default message.
     */
    public ClockException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ClockException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause the cause. Null is allowed.
     */
    public ClockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of (cause==null ? null : cause.toString()).
     *
     * @param cause the cause. Null is allowed.
     */
    public ClockException(Throwable cause) {
        super(cause);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
