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

import de.leifaktor.robbie.api.controllers.ClockException;

import java.util.Objects;

/**
 * RuntimeExceptionWrapper for ClockExceptions.
 *
 * <p>It is needed internally,since Runnables can't throw
 * checked Exceptions.
 */
public class ClockRuntimeException extends RuntimeException {

    /**
     * Needed for possible serialization.
     */
    private static final long serialVersionUID = 0xd389ff6fae1d4c17L;

    /**
     * Creates a new ClockRuntimeException with the given cause.
     *
     * @param cause the exception which caused this.
     */
    public ClockRuntimeException(ClockException cause) {
        super(Objects.requireNonNull(cause));
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
