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
 * This is the interface for inventories.
 *
 *<p>It provides access to the items.
 */
public interface Inventory {

    /**
     * Returns the weight of all items in this inventory.
     *
     * @return the weight of all items in this inventory.
     *         It is never negative.
     */
    double getWeight();

    /**
     * Adds an item to this inventory.
     *
     * @param item the item to add
     * @throws NullPointerException if item is null
     */
    void addItem(Item item);

    /**
     * Removes an item of this inventory.
     *
     * @param item the item to remove
     * @throws NullPointerException if item is null
     * @throws IllegalArgumentException if this inventory does not contain item.
     */
    void removeItem(Item item);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
