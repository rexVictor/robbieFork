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

import de.leifaktor.robbie.api.domain.Creature;
import de.leifaktor.robbie.api.domain.Field;
import de.leifaktor.robbie.api.domain.Item;
import de.leifaktor.robbie.api.domain.Tile;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Field is the leaf object in the world.
 */
public class FieldImpl extends IdClass implements Field {

    /**
     * The item stack.
     */
    private final Deque<Item> items = new ArrayDeque<>();

    /**
     * The creature currently on this field.
     *
     * <p>There can always only be one creature on one field.
     */
    private Creature occupant = null;

    /**
     * The tile of this field.
     */
    private Tile tile = null;

    /**
     * Creates a new field.
     *
     * @param id a unique id among all fields of a layer. Uniqueness is not checked.
     */
    public FieldImpl(long id) {
        super(id);
    }

    @Override
    public void setOccupyingCreature(Creature creature) {
        this.occupant = creature;
    }

    @Override
    public Creature getOccupyingCreature() {
        return occupant;
    }

    @Override
    public Deque<Item> getItems() {
        return items;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public Tile getTile() {
        return tile;
    }

}
/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
