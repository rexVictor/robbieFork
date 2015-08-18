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

import de.leifaktor.robbie.api.domain.Item;

/**
 * The base class for all items.
 */
public abstract class ItemImpl implements Item {

    /**
     * A unique id among all item instances.
     */
    private final long id;

    /**
     * The weight of this item.
     */
    private final double weight;

    /**
     * Creates a new item.
     *
     * @param id uniqe id among all instances; uniqueness is not checked.
     * @param weight the weigth of this item
     */
    public ItemImpl(long id, double weight) {
        this.id = id;
        this.weight = weight;
    }

}
