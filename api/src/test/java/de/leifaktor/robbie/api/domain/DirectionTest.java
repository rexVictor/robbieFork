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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * The testclass for the {@link Direction} enumeration.
 */
public class DirectionTest {

    /**
     * Constuctor for the test.
     */
    public DirectionTest() {
    }

    @Test(dataProvider = "northDirection")
    public void isNorth_NorthernDirections(Direction direction) {
        Assert.assertTrue(direction.isNorth());
    }

    @DataProvider(name = "northDirection")
    public Direction[][] getNorthDirections() {
        return new Direction[][] {
            { Direction.NORTHWEST },
            { Direction.NORTH },
            { Direction.NORTHEAST }
        };
    }

    @Test(dataProvider = "southDirection")
    public void isSouth_SouthernDirections(Direction direction) {
        Assert.assertTrue(direction.isSouth());
    }

    @DataProvider(name = "southDirection")
    public Direction[][] getSouthDirections() {
        return new Direction[][]{
            { Direction.SOUTHWEST },
            { Direction.SOUTH },
            { Direction.SOUTHEAST }
        };
    }

    @Test(dataProvider = "westDirection")
    public void isWest_WesternDirections(Direction direction) {
        Assert.assertTrue(direction.isWest());
    }

    @DataProvider(name = "westDirection")
    public Direction[][] getWestDirections() {
        return new Direction[][] {
            { Direction.SOUTHWEST },
            { Direction.WEST },
            { Direction.NORTHWEST }
        };
    }

    @Test(dataProvider = "eastDirection")
    public void isEast_EasternDirections(Direction direction) {
        Assert.assertTrue(direction.isEast());
    }

    @DataProvider(name = "eastDirection")
    public Direction[][] getEastDirections() {
        return new Direction[][]{
            { Direction.SOUTHEAST },
            { Direction.EAST },
            { Direction.NORTHEAST }
        };
    }

    @Test(dataProvider = "nonNorthDirection")
    public void isNorth_NonNorthernDirections(Direction direction) {
        Assert.assertFalse(direction.isNorth());
    }

    @DataProvider(name = "nonNorthDirection")
    public Direction[][] getNonNorthDirections() {
        return new Direction[][]{
            { Direction.WEST }, { Direction.EAST },
            { Direction.SOUTHWEST }, { Direction.SOUTH }, { Direction.SOUTHEAST }
        };
    }

    @Test(dataProvider = "nonSouthDirection")
    public void isSouth_NonSouthernDirections(Direction direction) {
        Assert.assertFalse(direction.isSouth());
    }

    @DataProvider(name = "nonSouthDirection")
    public Direction[][] getNonSouthDirections() {
        return new Direction[][]{
            { Direction.NORTHWEST }, { Direction.NORTH }, { Direction.NORTHEAST },
            { Direction.WEST }, { Direction.EAST }
        };
    }

    @Test(dataProvider = "nonWestDirection")
    public void isWest_NonWesternDirections(Direction direction) {
        Assert.assertFalse(direction.isWest());
    }

    @DataProvider(name = "nonWestDirection")
    public Direction[][] getNonWestDirections() {
        return new Direction[][]{
            { Direction.NORTH }, { Direction.NORTHEAST },
            { Direction.EAST },
            { Direction.SOUTH }, { Direction.SOUTHEAST }
        };
    }

    @Test(dataProvider = "nonEastDirection")
    public void isEast_NonEasternDirections(Direction direction) {
        Assert.assertFalse(direction.isEast());
    }

    @DataProvider(name = "nonEastDirection")
    public Direction[][] getNonEastDirections() {
        return new Direction[][]{
            { Direction.NORTHWEST }, { Direction.NORTH },
            { Direction.WEST },
            { Direction.SOUTHWEST }, { Direction.SOUTH }
        };
    }


    @Test(dataProvider = "invertedDirections")
    public void invert_allDirections(Direction direction, Direction expected) {
        Direction inverted = direction.invert();
        Assert.assertEquals(inverted, expected);
    }

    @DataProvider(name = "invertedDirections")
    public Direction[][] getInvertedDirections() {
        return new Direction[][] {
            { Direction.NORTHWEST, Direction.SOUTHEAST },
            { Direction.NORTH, Direction.SOUTH },
            { Direction.NORTHEAST, Direction.SOUTHWEST },
            { Direction.WEST, Direction.EAST }
        };
    }

    @Test(dataProvider = "allDirections")
    public void invert_all_isInvolution(Direction direction) {
        Direction twiceInverted = direction.invert().invert();
        Assert.assertEquals(direction, twiceInverted);
    }

    @DataProvider(name = "allDirections")
    public Direction[][] getAllDirections() {
        List<Direction[]> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            directions.add(new Direction[] { direction });
        }
        return directions.toArray(new Direction[][] {} );
    }

    @Test(dataProvider = "straightDirections")
    public void isStraight_straightDirections(Direction direction) {
        Assert.assertTrue(direction.isStraight());
    }

    @DataProvider(name = "straightDirections")
    public Direction[][] getStraightDirections() {
        return new Direction[][] {
            { Direction.NORTH }, { Direction.SOUTH },
            { Direction.WEST }, { Direction.EAST }
        };
    }

    @Test(dataProvider = "diagonalDirections")
    public void isStraight_diagonalDirections(Direction direction) {
        Assert.assertFalse(direction.isStraight());
    }

    @DataProvider(name = "diagonalDirections")
    public Direction[][] getDiagonalDirections() {
        return new Direction[][] {
            { Direction.NORTHWEST }, { Direction.SOUTHEAST },
            { Direction.NORTHEAST }, { Direction.SOUTHWEST }
        };
    }

    @Test(dataProvider = "straightDirections")
    public void isDiagonal_straightDirections(Direction direction) {
        Assert.assertFalse(direction.isDiagonal());
    }

    @Test(dataProvider = "diagonalDirections")
    public void isDiagonal_diagonalDirections(Direction direction) {
        Assert.assertTrue(direction.isDiagonal());
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
