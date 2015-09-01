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
 * Modifies the Speed.
*/
public interface SpeedModifier {

    /**
     * Modifies the speed.
     *
     * <p>It takes a double containing the speed and returns the modified speed.
     * The return value must always be non-negative.
     *
     * <p>Note: If a speed object is using multiple SpeedModifiers, the order
     * in which they are called may be unpredictable.
     *
     * @param speed the unmodified speed
     * @return the modified speed, which is always non-negative
     * @throws IllegalArgumentException if speed is negative
     */
    double modify(double speed);

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
