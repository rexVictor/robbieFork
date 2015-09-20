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
 * A CollisionHandler is called when two Collideables collide.
 *
 * <p>There must not be more than one handler for two concrete
 * subclasses of Collideable.
 *
 * <p>Implementations of CollisionHandlerProvider may require, that
 * for each pair of concrete Collideable subclasses there is a
 * specific CollisionHandler with these subclasses as types.
 * Therefore it is recommended, but not necessarily required, that
 * for all pairs of sub classes of Collideable there exists a
 * CollisionHandler with those types as generic parameters.
 *
 * @param <V> the type of the Collideable subclass this handler
 *           can handle
 * @param <W> the type of the Collideable subclass this handler
 *           can handle
 * @see CollisionHandlerProvider
 */
public interface CollisionHandler<V extends Collideable,W extends Collideable> {

    /**
     * Called whenever two instances of V and W collide.
     *
     * @param c1 one of the Collideables
     * @param c2 the other of the Collideables
     * @throws NullPointerException if c1 or c2 is null
     */
    void collisionHappened(V c1, W c2);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
