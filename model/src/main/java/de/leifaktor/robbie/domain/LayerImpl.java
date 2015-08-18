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

import de.leifaktor.robbie.api.domain.Field;
import de.leifaktor.robbie.api.domain.Layer;

import java.util.List;

/**
 * Layer is a wrapper object for all Rooms .
 *
 * <p>This contains all fields and provides access to them.
 */
public class LayerImpl implements Layer {

    /**
     * A uniqe ID among all layers of a Room.
     */
    private final long id;

    /**
     * A list of list of all fields.
     * 
     * <p> The outer list holds all coloumns and all inner lists have the
     * same size.
     *
     * <p> All lists may be immutable.
     */
    private final List<List<Field>> fields;

    /**
     * Creates a new Layer with the specified id and the specified fields.
     *
     * <p> The id must be unique among all layers of a room.
     *
     * @param id unique id among all layers of a Room. Uniqueness is not checked.
     * @param fields the fields this layer has
     * @throws NullPointerException if fields is null or any of its sublists are null
     * @throws IndexOutOfBoundsException if any list is empty or if the entry lists,
     *         do not have same length
     */
    public LayerImpl(long id, List<List<Field>> fields) {
        this.id = id;
        this.fields = Validators.requireNonNullAndSameSize(fields);
    }

    @Override
    public Field getField(int x, int y) {
        return fields.get(x).get(y);
    }

}
/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
