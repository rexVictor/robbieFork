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

import de.leifaktor.robbie.api.domain.Layer;
import de.leifaktor.robbie.api.domain.Room;

import java.util.List;

/**
 * Room is a wrapper object for fields.
 */
public class RoomImpl implements Room {

    /**
     * A unique id among all room instances belonging to a floor.
     */
    private final long id;

    /**
     * The number of Fields each row in each layer has.
     */
    private final int width;

    /**
     * The number of Fields each coloum in each layer has.
     */
    private final int height;

    /**
     * A list of Layers belonging to a Room.
     *
     * <p>Can be immutable.
     */
    private final List<Layer> layers;

    /**
     * Creates a new Room.
     *
     *
     * @param id A unique id among all room instances among a floor.
     *           The constructor does not check uniqueness!
     * @param width the number of fields each row in each layer has.
     * @param height the number of fields each height in each layer has.
     * @param layers A list of all {@code Layers} of this Room. The list maybe immutable.
     *         The lowest layer is indexed with 0, the second lowest with 1, etc.
     * @throws NullPointerException if layers is null.
     * @throws IndexOutOfBoundsException if layers is empty or width or height is not positive.
     */
    public RoomImpl(long id, int width, int height, List<Layer> layers) {
        // checks if layers is null or empty
        layers.get(0);
        if (width <= 0 || height <= 0) {
            throw new IndexOutOfBoundsException();
        }
        this.id = id;
        this.width = width;
        this.height = height;
        this.layers = layers;
    }

    @Override
    public Layer getLayer(int z) {
        return layers.get(z);
    }

    @Override
    public int layerCount() {
        return layers.size();
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
