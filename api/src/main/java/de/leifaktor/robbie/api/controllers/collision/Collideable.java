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

package de.leifaktor.robbie.api.controllers.collision;

/**
 * This interface is implemented by all classes which represent an entity which can collide
 * with another entity.
 *
 * <p>All implementations need to specify a CollisionHandler which shall handle collisions.
 *
 * @see CollisionHandler
 */
public interface Collideable {

    /**
     * Returns the current position in the room.
     * @return a DoublePosition object representing the current position
     *         of this in a room; it is never null
     */
    DoublePosition getPosition();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
