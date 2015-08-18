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

import java.util.Collection;

/**
 * Field is the leaf object in the world.
 */
public interface Field {

    /**
     * Sets the creature occupying this field.
     *
     * @param creature the creature to occupy this field
     */
    void setOccupyingCreature(Creature creature);

    /**
     * Returns the creature occupying this field.
     *
     * @return the creature on this field if any and null otherwise
     */
    Creature getOccupyingCreature();

    /**
     * Returns the items lying on this field.
     *
     * @return a collection of items lying on this field.
     */
    Collection<Item> getItems();

    /**
     * Sets the tile of this field.
     *
     * @param tile the tile to be set, may not be null.
     * @throws NullPointerException if tile is null.
     */
    void setTile(Tile tile);

    /**
     * Returns the tile of this field.
     *
     * @return the tile of this field. It ist never null.
     */
    Tile getTile();

}
/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
