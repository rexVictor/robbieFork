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
 * All directions a creature can move to.
 */
public enum Direction {

    /**
     * Northwest direction.
     */
    NORTHWEST {
        @Override
        public Direction invert() {
            return SOUTHEAST;
        }

        @Override
        public boolean isStraight() {
            return false;
        }
    },

    /**
     * North direction.
     */
    NORTH {
        @Override
        public Direction invert() {
            return SOUTH;
        }

        @Override
        public boolean isStraight() {
            return true;
        }
    },

    /**
     * Northeast direction.
     */
    NORTHEAST {
        @Override
        public Direction invert() {
            return SOUTHWEST;
        }

        @Override
        public boolean isStraight() {
            return false;
        }
    },

    /**
     * West direction.
     */
    WEST {
        @Override
        public Direction invert() {
            return EAST;
        }

        @Override
        public boolean isStraight() {
            return true;
        }
    },

    /**
     * East direction.
     */
    EAST {
        @Override
        public Direction invert() {
            return WEST;
        }

        @Override
        public boolean isStraight() {
            return true;
        }
    },

    /**
     * Southwest direction.
     */
    SOUTHWEST {
        @Override
        public Direction invert() {
            return NORTHEAST;
        }

        @Override
        public boolean isStraight() {
            return false;
        }
    },

    /**
     * South direction.
     */
    SOUTH {
        @Override
        public Direction invert() {
            return NORTH;
        }

        @Override
        public boolean isStraight() {
            return true;
        }
    },

    /**
     * Southeast direction.
     */
    SOUTHEAST {
        @Override
        public Direction invert() {
            return NORTHWEST;
        }

        @Override
        public boolean isStraight() {
            return false;
        }
    };

    /**
     * Returns true if and only if this direction is NORTHWEST, NORTH or NORTHEAST.
     * @return if this direction has a component to north
     */
    public boolean isNorth() {
        return name().startsWith(NORTH.name());
    }

    /**
     * Returns true if and only if this direction is SOUTHWEST, SOUTH or SOUTHEAST.
     * @return if this direction has a component to south
     */
    public boolean isSouth() {
        return name().startsWith(SOUTH.name());
    }

    /**
     * Returns true if and only if this direction is NORTHWEST, WEST or SOUTHWEST.
     * @return if this direction has a component to west
     */
    public boolean isWest() {
        return name().endsWith(WEST.name());
    }

    /**
     * Returns true if and only if this direction is NORTHEAST, EAST or SOUTHEAST.
     * @return if this direction has a component to east
     */
    public boolean isEast() {
        return name().endsWith(EAST.name());
    }

    /**
     * Returns the inverted direction of this direction.
     *
     * <p> e.g. NORTHWEST.invert() == SOUTHEAST, SOUTH.invert() == NORTH
     * @return the inverted direction.
     */
    public abstract Direction invert();

    /**
     * Returns true if and only if this direction is NORTH, SOUTH, EAST or WEST.
     * @return if this direction is straight.
     */
    public abstract boolean isStraight();

    /**
     * Returns true if and olny if this direction in neither NORTH, SOUTH, EAST nor WEST.
     * @return if the direction is diagonal.
     */
    public boolean isDiagonal() {
        return !isStraight();
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
