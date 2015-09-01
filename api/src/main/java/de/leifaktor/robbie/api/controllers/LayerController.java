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

/**
 * This defines how to handle a Layer.
 *
 * <p>Since a Layer stores several Fields, it is the responsibility
 * of a LayerController to handle several FieldControllers.
 * It provides methods to allow underlying FieldControllers to communicate.
 *
 * <p>Implementations need to synchronize the model and the GUI.
 * They also need to notify other controllers about occured events.
 */
public interface LayerController {

    /**
     * Returns the field in row x coloumn y.
     *
     * @param x row
     * @param y coloumn
     * @return the field at (x,y).
     * @throws IndexOutOfBoundsException if there is no field at (x,y)
     */
    FieldController getField(int x, int y);

    /**
     * Returns the room conroller, holding this layer.
     *
     * @return the room controller
     */
    RoomController getRoomController();

    /**
     * Stops all creatures on this layer.
     */
    void freeze();

    /**
     * Resumes all creatuers on this layer.
     */
    void unfreeze();
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
