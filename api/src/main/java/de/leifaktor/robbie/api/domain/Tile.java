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

/**
 * A tile holds shared information between fields.
 *
 * <p>The graphical representation of a field or slowing effects
 * are stored in here.
 */
public interface Tile {

    /**
     * Returns true if and only if this Tile can be entered by creature.
     *
     * @param creature the CreatureController controlling the creature
     *        trying to enter
     * @return if this tile can be entered
     * @throws NullPointerException if creature is null
     */
    boolean canEnter(CreatureController creature);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
