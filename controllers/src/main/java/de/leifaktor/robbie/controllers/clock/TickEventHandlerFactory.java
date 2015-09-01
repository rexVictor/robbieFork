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

import de.leifaktor.robbie.api.controllers.ClockListener;

import java.util.Collection;

/**
 * The interface for creating TickEventHandlers.
 *
 */
public interface TickEventHandlerFactory {

    /**
     * Creates a new TickEventHandler.
     * @param listeners the clocklisteners to be notified
     * @return a TickEventHandler
     */
    TickEventHandler create(Collection<? extends ClockListener> listeners);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
