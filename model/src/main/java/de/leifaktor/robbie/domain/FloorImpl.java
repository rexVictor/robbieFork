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

package de.leifaktor.robbie.domain;


import de.leifaktor.robbie.api.domain.Floor;
import de.leifaktor.robbie.api.domain.Room;

import java.util.List;

/**
 * Floor is a wrapper object for all Rooms .
 *
 * <p>This contains all Rooms and provides access to them.
 */
public class FloorImpl extends IdClass implements Floor {

    /**
     * A list of list of rooms.
     *
     * <p>Neither this list nor its entry lists may be empty or null.
     *     All entry list must have the same length.
     */
    private final List<List<Room>> rooms;

    /**
     * Creates a new Floor with the specified id and the specified rooms.
     *
     * <p>The id must be unique among all floors of an episode.
     *
     * @param id unique id among all floors of an Episode. Uniqueness is not checked.
     * @param rooms the rooms this floor has
     * @throws NullPointerException if rooms is null or any of it sublists is null
     * @throws IndexOutOfBoundsException if any list is empty or if the entry lists,
     *         do not have same length
     */
    public FloorImpl(long id, List<List<Room>> rooms) {
        super(id);
        this.rooms = Validators.requireNonNullAndSameSize(rooms);
    }

    @Override
    public Room getRoom(int x, int y) {
        return rooms.get(x).get(y);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
