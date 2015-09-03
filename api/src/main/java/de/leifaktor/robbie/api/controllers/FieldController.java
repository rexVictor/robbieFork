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

package de.leifaktor.robbie.api.controllers;

import de.leifaktor.robbie.api.domain.Direction;


/**
 * This defines how to handle an Field.
 *
 * <p>Implementations need to synchronize the model and the GUI.
 * They also need to notify other controllers about occurred events.
 */
public interface FieldController {

    /**
     * Returns true if and only if the creature can enter this field.
     *
     * <p>The return value may be dependent on other things, like the Tile
     * of the Field or the items on it. So this method does not need to be pure.
     *
     * @param creature the creature to check if it can enter
     * @return if creature can enter this field
     */
    boolean canEnter(CreatureController creature);

    /**
     * Returns the LayerController this FieldController corresponds to.
     * @return the layer controller of the layer of the fields
     *         controlled by this FieldController.
     *         It is never null.
     */
    LayerController getLayerController();

    /**
     * Checks if fieldController is next to this.
     *
     * <p>Specifically: Checks if fieldController controls a field,
     * which is next to the field controlled by this FieldController.
     *
     * @param fieldController the controller to check if its field
     *        is next to this ones.
     * @return the Direction to go from fieldControllers field
     *         to reach this one, if they are next to each other
     *         and null otherwise
     * @throws NullPointerException if fieldController is null
     * @throws IllegalArgumentException if fieldController
     *         controls a field, which is not in the same layer
     *         as the one controlled by this.
     */
    Direction isNextTo(FieldController fieldController);

    /**
     * Stops all creatures on this field.
     */
    void freeze();

    /**
     * Resumes all creatures on his field.
     */
    void unfreeze();

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
