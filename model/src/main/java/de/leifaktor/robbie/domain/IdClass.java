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

/**
 * A class which holds a long id value.
 */
public class IdClass {

    /**
     * A unique id.
     */
    private final long id;

    /**
     * Creates a new IdClass object.
     *
     * @param id a uniqe id. Uniqueness is not checked.
     */
    public IdClass(long id) {
        this.id = id;
    }

    /**
     * Returns the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }
}
