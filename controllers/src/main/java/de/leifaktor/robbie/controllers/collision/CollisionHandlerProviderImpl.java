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
 * Implements the CollisionHandlerProvider interface.
 *
 * <p>This implementation requires that for each pair of Collideable
 * subclasses there is exactly one CollisionHandler for that pair.</p>
 */
public class CollisionHandlerProviderImpl implements CollisionHandlerProvider {

    /**
     * The Class which does the actual mapping in a type safe fashion.
     */
    private final ClassMap classMap = new ClassMap();

    /**
     * Creates a new CollisionHandlerProvider.
     */
    public CollisionHandlerProviderImpl() {
        super();
    }

    @Override
    public <V extends Collideable, W extends Collideable> CollisionHandler<V, W>
                getHandlerFor(V c1, W c2) {
        //This is ridiculous: c1.getClass() returns an object of
        //type Class<? extends Collideable> instead of Class<V>.
        //One might expect the compiler to be a little smarter...
        //If someone has a solution doing this without casting, feel free to contact me
        //I hate casting(-shows)...
        @SuppressWarnings("unchecked")
        Class<V> clazz1 = (Class<V>) c1.getClass();
        @SuppressWarnings("unchecked")
        Class<W> clazz2 = (Class<W>) c2.getClass();
        return classMap.get(clazz1, clazz2);
    }

    @Override
    public <V extends Collideable, W extends Collideable> void register(
            Class<V> clazz1, Class<W> clazz2, CollisionHandler<V,W> handler) {
        classMap.put(clazz1, clazz2, handler);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
