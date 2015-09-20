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

import java.util.Objects;

/**
 * Wraps a pair of Class objects which have a common super type.
 *
 * @param <S> the common super type of V and W
 * @param <V> the type of one class
 * @param <W> the type of the other class
 */
public class ClassPair<S, V extends S, W extends S> {

    /**
     * First Class object.
     */
    private final Class<V> clazz1;

    /**
     * Second Class object.
     */
    private final Class<W> clazz2;

    /**
     * Creates a new ClassPair.
     *
     * @param clazz1 first class
     * @param clazz2 second class
     * @throws NullPointerException if clazz1 or clazz2 is null
     */
    public ClassPair(Class<V> clazz1, Class<W> clazz2) {
        this.clazz1 = Objects.requireNonNull(clazz1);
        this.clazz2 = Objects.requireNonNull(clazz2);
    }

    /**
     * Returns true if and only if other is equal to this.
     *
     * <p>Two ClassPairs are considered equal if and only if
     * the contain the same Class objects regardless of their order.
     * @param other the object to check equality for
     * @return if other is equal to this
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other instanceof ClassPair) {
            ClassPair<?, ?, ?> otherPair = (ClassPair<?, ?, ?>) other;
            return (clazz1 == otherPair.clazz1 && clazz2 == otherPair.clazz2)
                    || (clazz1 == otherPair.clazz2 && clazz2 == otherPair.clazz1);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return clazz1.hashCode() ^ clazz2.hashCode();
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
