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

package de.leifaktor.robbie.controllers.collision;

import de.leifaktor.robbie.api.controllers.collision.Collideable;

/**
 * Wraps a pair of Class objects which have a common super type.
 *
 * @param <V> the type of one class
 * @param <W> the type of the other class
 */
public class CollideableClassPair<V extends Collideable, W extends Collideable>
        extends ClassPair<Collideable, V, W> {

    /**
     * Creates a new CollideableClassPair.
     *
     * @param clazz1 first class
     * @param clazz2 second class
     * @throws NullPointerException if clazz1 or clazz2 is null
     */
    public CollideableClassPair(Class<V> clazz1, Class<W> clazz2) {
        super(clazz1, clazz2);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
