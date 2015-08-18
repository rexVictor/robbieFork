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

package de.leifaktor.robbie.api.domain;

import de.leifaktor.robbie.api.controllers.CreatureController;
import de.leifaktor.robbie.api.controllers.FieldController;

/**
 * The item interface.
 */
public interface Item {

    /**
     * Checks if this item can be used.
     *
     * @param field the FieldController controlling the field this item might be used on.
     * @param creature the CreatureController controlling the creature trying to use this item.
     * @return if this item can be used by creature on field
     * @throws NullPointerException if field or creature is null.
     */
    boolean canUse(FieldController field, CreatureController creature);

    /**
     * Checks if this item can be dropped.
     *
     * @param field the FieldController controlling the field this item might be dropped on.
     * @param creature the CreatureController controlling the creature trying to drop this item.
     * @return if this item can be dropped by creature on field
     * @throws NullPointerException if field or creature is null.
     */
    boolean canDrop(FieldController field, CreatureController creature);

    /**
     * Returns a Command object specifying the actions taken on use.
     *
     * @param field the field this item is used on.
     * @param creature the creature using this item.
     * @return a command object. It is never null.
     * @throws NullPointerException if field or creature is null.
     */
    Command use(FieldController field, CreatureController creature);

    /**
     * Returns a Command object specifying the actions taken on drop.
     *
     * @param field the field this item is dropped on.
     * @param creature the creature dropping this item.
     * @return a command object. It is never null.
     * @throws NullPointerException if field or creature is null.
     */
    Command drop(FieldController field, CreatureController creature);

    /**
     * Returns if creature can move on a field, where this item is lying.
     *
     * @param creature the creature to check if it can.
     * @return if the creature can move on a field with this item.
     * @throws NullPointerException if creature is null.
     */
    boolean canMoveOn(CreatureController creature);

}
/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
