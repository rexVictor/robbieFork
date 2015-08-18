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

package de.leifaktor.robbie.api.domain;

/**
 * Floor is a wrapper object for all Rooms .
 *
 * <p>This contains all Rooms and provides access to them.
 */
public interface Floor {

    /**
     * Returns the room in row x coloumn y.
     *
     * @param x row
     * @param y coloumn
     * @return the room at (x,y).
     * @throws IndexOutOfBoundsException if there is no room at (x,y)
     */
    Room getRoom(int x, int y);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
