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
 * A wrapper class for two doubles representing the coordinates of Collideables.
 */
public class DoublePosition {

    /**
     * The xPos coordinate.
     */
    private final double xPos;

    /**
     * The yPos coordinate.
     */
    private final double yPos;

    /**
     * Creates a new DoublePosition instance.
     * @param xPos the xPos coordinate
     * @param yPos the yPos coordinate
     */
    public DoublePosition(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Returns the square of the euclidean distance between this and other.
     *
     * <p>The square is returned because of performance reasons.
     *
     * @param other the DoublePosition to calculate the distance to
     * @return {@code (this.xPos - other.xPos)^2 + (this.yPos - other.yPos)^2}
     * @throws NullPointerException if other is null
     */
    public double distanceTo(DoublePosition other) {
        double xDiff = xPos - other.xPos;
        double yDiff = yPos - other.yPos;
        return xDiff * xDiff + yDiff * yDiff;
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
