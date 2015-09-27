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
import de.leifaktor.robbie.api.controllers.collision.CollisionDetector;
import de.leifaktor.robbie.api.controllers.collision.CollisionHandlerProvider;

import java.util.Collection;
import java.util.Objects;

/**
 * A limit based implementation of CollisionDetector.
 *
 * <p>This implementation detects a collision between two
 * Collideables if their distance is smaller than the limit
 * set on construction.
 */
public class CollisionDetectorImpl implements CollisionDetector {

    /**
     * The distance under which a collision is detected.
     */
    private final double limit;

    /**
     * The CollisionHandlerProvider used for handing collisions.
     */
    private final CollisionHandlerProvider provider;

    /**
     * Creates a new CollisionDetector.
     *
     * @param limit the threshold for detecting a collision
     * @param provider the CollisionHandlerProvider providing CollisionHandlers
     * @throws NullPointerException if provider is null
     * @throws IllegalArgumentException if limit is not positive
     */
    public CollisionDetectorImpl(double limit, CollisionHandlerProvider provider) {
        if (limit <= 0) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
        this.provider = Objects.requireNonNull(provider);
    }

    @Override
    public void doDetection(Collection<? extends Collideable> collideables) {
        collideables.stream().flatMap(c1 ->
                collideables.stream().map(c2 -> makePair(c1, c2)))
                .filter(pair -> pair.distance() < limit)
                .forEach(pair -> pair.collide(provider));
    }

    /**
     * Creates a CollideablePair.
     *
     * @param collideable1 the first Collideable
     * @param collideable2 the second Collideable
     * @param <V> the type of collideable1
     * @param <W> the type of collideable2
     * @return a CollideablePair containing collideable1 and collideable2
     * @throws NullPointerException if collideable1 or collideable2 is null
     */
    private <V extends Collideable, W extends Collideable>
                CollideablePair<V, W> makePair(V collideable1, W collideable2) {
        return new CollideablePair<>(collideable1, collideable2);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
