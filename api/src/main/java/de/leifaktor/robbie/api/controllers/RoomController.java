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
 * This defines how to handle a Room.
 *
 * <p>Since a Floor stores several Rooms, it is the responsibility
 * of a FloorController to handle several RoomControllers.
 * It provides methods to allow underlying RoomControllers to communicate.
 *
 * <p>Implementations need to synchronize the model and the GUI.
 * They also need to notify other controllers about occured events.
 */
public interface RoomController {

    /**
     * Returns the FloorController holding this RoomController.
     *
     * @return the FloorController
     */
    FloorController getFloorController();

    /**
     * Enters this room.
     *
     * @param fromRoom the room the creature is coming from
     * @param fromField the field the creature is coming from
     * @param direction the direction the creature is walking to.
     *        Null indicates a teleport
     * @param creature the creature entering
     */
    void enter(RoomController fromRoom, FieldController fromField,
            Direction direction, CreatureController creature);

    /**
     * Leaves this room.
     *
     * @param toRoom the room the creature is going to
     * @param fromField the field the creature is coming from
     * @param direction the direction the creature is walking to.
     *        Null indicates a teleport
     * @param creature the creature leaving
     */
    void leave(RoomController toRoom, FieldController fromField,
            Direction direction, CreatureController creature);

    /**
     * Spawn a new creature.
     *
     * @param field the field the creature is spawned on.
     * @param creature the creature to spewn
     */
    void spawn(FieldController field, CreatureController creature);
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
