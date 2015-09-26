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
 * A Pair object wrapping typesafe two objects and
 * providing an equals() implementation.
 *
 * @param <V> the type of one wrapped object
 * @param <W> the type of the other wrapped object
 * @see #equals(Object)
 */
public class Pair<V,W> {

    /**
     * First object.
     */
    protected final V object1;

    /**
     * Second object.
     */
    protected final W object2;

    /**
     * Creates a new Pair.
     *
     * @param object1 first object
     * @param object2 second object
     * @throws NullPointerException if object1 or object2 is null
     */
    public Pair(V object1, W object2) {
        this.object1 = Objects.requireNonNull(object1);
        this.object2 = Objects.requireNonNull(object2);
    }

    /**
     * Returns true if and only if other is equal to this.
     *
     * <p>Two Pairs are considered equal if and only if
     * they contain equal objects according to their equals()
     * implementation regardless of their order.
     *
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
        if (other instanceof Pair) {
            Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return (object1.equals(otherPair.object1) && object2.equals(otherPair.object2))
                    || (object1.equals(otherPair.object2) && object2.equals(otherPair.object1));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return object1.hashCode() + object2.hashCode();
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
