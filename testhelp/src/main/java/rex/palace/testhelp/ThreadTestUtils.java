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

package rex.palace.testhelp;

import java.util.concurrent.TimeUnit;

/**
 * A utility class providing helper methods for testing Threads.
 */
public final class ThreadTestUtils {

    /**
     * Empty Constructor since this is a utility class.
     */
    private ThreadTestUtils() {
    }

    /**
     * Waits till thread is in state.
     *
     * @param thread the tread to monitor
     * @param state the state to wait for
     * @throws InterruptedException if the calling thread gets interrupted
     */
    public static void waitTillThreadInState(
            Thread thread, Thread.State state) throws InterruptedException {
        while (thread.getState() != state) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
