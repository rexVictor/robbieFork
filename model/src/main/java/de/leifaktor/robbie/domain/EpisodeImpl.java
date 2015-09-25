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

package de.leifaktor.robbie.domain;

import de.leifaktor.robbie.api.domain.Episode;
import de.leifaktor.robbie.api.domain.Floor;

import java.util.List;

/**
 * Episode describes the variable data of the entire world.
 *
 * <p>It stores all {@link Floor}s.
 */
public class EpisodeImpl extends IdClass implements Episode {

    /**
     * List of Floors.
     *
     * <p>Can be immutable.
     */
    private final List<Floor> floors;

    /**
     * Creates a new Episode.
     *
     * @param id A unique id among all Episode instances. The constructor does not check uniqueness!
     * @param floors A list of all {@code Floors} of this Episode. The list maybe immutable.
     *        The highest floor is indexed with 0, the second highest with 1, etc.
     * @throws NullPointerException if floors is null.
     * @throws IndexOutOfBoundsException if floors is empty.
     */
    public EpisodeImpl(long id, List<Floor> floors) {
        super(id);
        if (floors.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        this.floors = floors;
    }

    @Override
    public Floor getFloor(int z) {
        return floors.get(z);
    }

    @Override
    public int floorCount() {
        return floors.size();
    }
}
/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
