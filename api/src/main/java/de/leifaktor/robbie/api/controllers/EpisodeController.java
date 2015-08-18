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
 * This defines how to handle an Episode.
 *
 * <p>Since an episode stores several Floors, it is the responsibility
 * of an EpisodeController to handle several FloorControllers.
 * It provides methods to allow underlying FloorControllers to communicate.
 *
 * <p>Implementations need to synchronize the model and the GUI.
 * They also need to notify other controllers about occured events.
 */
public interface EpisodeController {

    /**
     * Returns the controller of the z-th highest floor.
     *
     * @param z The FloorController to get. The highest floor is returned if z equals 0,
     *          the second highest if z equals 1, etc.
     * @return the FloorController controlling the z-th highest floor.
     * @throws IndexOutOfBoundsException if there is no z-th floor.
     *         (<tt>z &lt; 0 || z &gt;= floorCount()</tt>)
     */
    FloorController getFloor(int z);

    /**
     *  Returns the number of floors of the episode this controls.
     *
     *  @return the number of floors of the episode this controls;
     *          it is always positive.
     */
    int floorCount();
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
