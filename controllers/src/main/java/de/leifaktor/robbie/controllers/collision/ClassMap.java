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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.leifaktor.robbie.api.controllers.collision.Collideable;
import de.leifaktor.robbie.api.controllers.collision.CollisionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstraction layer for a mapping used by CollisionHandlerProviderImpl.
 *
 * <p>It wraps a &lt;T extends Collideable, U extends Collideable&gt;
 * Map&lt;CollideableClassPair&lt;Collideable,T,U&gt;, CollisionHandler&lt;T,U&gt;&gt; meaning
 * that for any valid T,U an instance of CollideableClassPair&lt;Collideable,T,U&gt; is
 * mapped to an instance of CollisionHandler&lt;T,U&gt;.
 *
 * <p>Since Java is currently lacking such a type safety feature, the
 * type safety is guaranteed by implementation rather than by the compiler.
 */
public class ClassMap {

    /**
     * The Table containing the CollisionHandlers.
     */
    private Table<Class<? extends Collideable>, Class<? extends Collideable>, CollisionHandler<?,?>>
        handlers = HashBasedTable.create();

    /**
     * Creates a new ClassMap.
     */
    public ClassMap() {
        super();
    }

    /**
     * Puts a mapping of clazz1, clazz2 to handler in the wrapped map.
     *
     * <p>This method ensures the type safety for the purpose of this class.
     *
     * @param clazz1 a class object of T
     * @param clazz2 a class object of U
     * @param handler the handler handling instances of T and U
     * @param <V> the type of one Collideable subclass
     * @param <W> the type of the other Collideable subclass
     */
    public <V extends Collideable, W extends Collideable> void put(
            Class<V> clazz1, Class<W> clazz2, CollisionHandler<V, W> handler) {
        handlers.put(clazz1, clazz2, handler);
    }

    /**
     * Returns a CollisionHandler for instances of clazz1 and clazz2.
     *
     * <p>This method ensure the type safety for the purpose of this class.
     *
     * @param clazz1 the class object of T
     * @param clazz2 the class object of U
     * @param <T> the type of one Collideable subclass
     * @param <U> the type of the other Collideable subclass
     * @return a CollisionHandler&lt;T,U&gt;
     */
    @SuppressWarnings("unchecked")
    public <T extends Collideable, U extends Collideable>
            CollisionHandler<T, U> get(
            Class<T> clazz1, Class<U> clazz2) {
        //Implementation of put(.,.,.) guarantees this cast to be valid
        //If someone has a solution doing this without casting feel free to contact me
        return (CollisionHandler<T, U>) handlers.get(clazz1, clazz2);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */