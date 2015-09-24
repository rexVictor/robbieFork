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

package de.leifaktor.robbie.api.controllers.collision;

/**
 * This interface defines how CollisionHandlers are provided.
 *
 * <p>There must not be more than one CollisionHandler handling
 * two subclasses of Collideable.
 *
 * <p>Implementations may require that for each pair of
 * subclasses of Collideable is exactly one CollisionHandler handling
 * these subclasses.
 */
public interface CollisionHandlerProvider {

    /**
     * Returns a CollisionHandler handling the arguments.
     *
     * @param <V> the type of c1
     * @param <W> the type of c2
     * @param c1 one of the Collideables colliding
     * @param c2 the other one
     * @return a CollisionHandler for c1 and c2; never null
     */
    <V extends Collideable, W extends Collideable> CollisionHandler<? super V, ? super W>
                getHandlerFor(V c1, W c2);

    /**
     * Registers a CollisionHandler for specific types.
     *
     * @param clazz1 the class object of a subclass of Collideable
     * @param clazz2 the other class object
     * @param handler a CollisionHandler handling collisions
     *                between instances of clazz1 and clazz2
     * @param <V> the type of clazz1 instances
     * @param <W> the type of class2 instances
     */
    <V extends Collideable, W extends Collideable> void register(
            Class<V> clazz1, Class<W> clazz2, CollisionHandler<V,W> handler);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
