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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the DoublePosition class.
 */
public class DoublePositionTest {

    /**
     * The difference doubles may differ to be equal.
     */
    private static final double ACCURACY = 1E-7;

    /**
     * Empty constructor.
     */
    public DoublePositionTest() {
        super();
    }

    @DataProvider( name = "pointsToZero")
    public Object[][] positionDistance() {
        return new Object[][]{
                {0,0,0}, {0,1,1}, {1,0,1}, {0,2,4}, {2,0,4},
                {1,1,2}, {1,2,5}, {2,1,5}, {1,3,10}, {3,1, 10},
                {0.5, 0.5, 0.5}, {0.25, 0.5, 0.3125}, {1.1, 0, 1.21},
                {0,0,0}, {0,-1,1}, {-1,0,1}, {0,-2,4}, {-2,0,4},
                {-1,-1,2}, {-1,-2,5}, {-2,-1,5}, {-1,-3,10}, {-3,-1, 10},
                {-0.5, -0.5, 0.5}, {-0.25, -0.5, 0.3125}, {-1.1, 0, 1.21}
        };
    }

    @DataProvider(name = "differentPoints")
    public Object[][] positionsAndDistance() {
        return new Object[][]{
                {0,1,1,0,2}, {0,1,0,2,1}, {1,0,1,0,0}, {0.5, 1, 4, 0.25, 12.8125}
        };
    }

    @Test(dataProvider = "pointsToZero")
    public void distanceTo_ZeroPosition(double x, double y, double expected) {
        DoublePosition zeroPosition = new DoublePosition(0,0);
        DoublePosition other = new DoublePosition(x,y);

        double distance = zeroPosition.distanceTo(other);

        Assert.assertEquals(distance, expected, ACCURACY);
    }

    @Test(dataProvider = "differentPoints")
    public void distanceTo(double x1, double y1, double x2, double y2, double expected) {
        DoublePosition firstPosition = new DoublePosition(x1, y1);
        DoublePosition secondPosition = new DoublePosition(x2, y2);

        double distance = firstPosition.distanceTo(secondPosition);

        Assert.assertEquals(distance, expected, ACCURACY);
    }

}
