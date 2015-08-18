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
 * Room is a wrapper object for layers.
 */
public interface Room {

    /**
     * Returns the {@code z}-th lowest layer of this Room.
     *
     * @param z The Layer to get. The lowest layer is returned if z equals 0,
     *          the second lowest if z equals 1, etc.
     * @return the z-th lowest layer.
     * @throws IndexOutOfBoundsException if there is no {@code z}-th layer.
     *         (<tt>z &lt; 0 || z &gt;= size()</tt>)
     */
    Layer getLayer(int z);

    /**
     *  Returns the number of layers in this room.
     *
     *  @return the number of layers in this room. It is always greater than zero.
     */
    int layerCount();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
