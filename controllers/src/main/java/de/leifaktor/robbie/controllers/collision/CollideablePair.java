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
import de.leifaktor.robbie.api.controllers.collision.CollisionHandler;
import de.leifaktor.robbie.api.controllers.collision.CollisionHandlerProvider;

/**
 * A subclass of Pair specifically for Pairs of Collideables.
 *
 * @param <V> the type of one Collideable this wraps
 * @param <W> the type of the other Collideable this wraps
 */
public class CollideablePair<V extends Collideable, W extends Collideable> extends Pair<V,W>
        implements Comparable<CollideablePair<V,W>> {

    /**
     * Creates a new CollideablePair.
     *
     * @param collideable1 first collideable
     * @param collideable2 second collideable
     * @throws NullPointerException if collideable1 or collideable2 is null
     */
    public CollideablePair(V collideable1, W collideable2) {
        super(collideable1, collideable2);
    }

    /**
     * Returns the current distance between the wrapped Collideables.
     *
     * <p>The distance is determined by {@link
     * de.leifaktor.robbie.api.controllers.collision.DoublePosition#distanceTo(DoublePosition)}.
     *
     * @return the distance between the wrapped Collideables
     * @see de.leifaktor.robbie.api.controllers.collision.DoublePosition#distanceTo(DoublePosition)
     */
    public double distance() {
        return object1.getPosition().distanceTo(object2.getPosition());
    }

    @Override
    public int compareTo(CollideablePair<V, W> other) {
        return Double.compare(distance(), other.distance());
    }

    /**
     * Lets the wrapped Collideables collide.
     *
     * @param provider the CollisionHandlerProvider to obtain
     *                 the CollisionHandler for the collision
     * @throws NullPointerException if provider is null
     */
    public void collide(CollisionHandlerProvider provider) {
        CollisionHandler<V,W> handler = provider.getHandlerFor(object1, object2);
        handler.collisionHappened(object1, object2);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
