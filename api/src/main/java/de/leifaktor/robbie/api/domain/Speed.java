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

/**
 * Describes the speed of a creature.
 */
public interface Speed {

    /**
     * Adds a SpeedModifier to this Speed object.
     *
     * @param modifier a SpeedModifier to use when calculating the speed
     * @throws NullPointerException if modifier is null
     */
    void addModifier(SpeedModifier modifier);

    /**
     * Removes a SpeedModifier to this Speed object.
     *
     * @param modifier a SpeedModifier to not use anymore when calculating the speed
     * @throws NullPointerException if modifier is null
     * @throws IllegalArgumentException if modifier was not added
     */
    void removeModifier(SpeedModifier modifier);

    /**
     * Returns the current speed.
     *
     * <p>Note: The speed may be dependent on other things, so
     * subsequent callings must not return the same speed.
     *
     * @return the current speed, always non-negative
     */
    double getCurrentSpeed();

    /**
     * Returns the minimum speed.
     *
     * @return the minimum speed
     */
    double getMinimumSpeed();

    /**
     * Returns the maximum speed.
     *
     * @return the maximum speed
     */
    double getMaximumSpeed();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
