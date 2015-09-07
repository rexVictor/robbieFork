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

import java.util.concurrent.Callable;

/**
* A Thread helping testing.
*/
public class TestThread extends Thread {

    /**
     * The exception thrown by run.
     */
    private Exception exception;

    /**
     * The callable to actually run.
     */
    private final Callable<?> callable;

    /**
     * Creates a new TestThread.
     *
     * @param callable the callable to run
     */
    public TestThread(Callable<?> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            callable.call();
        } catch (Exception e) {
            exception = e;
        }
    }

    /**
     * Execute in current Thread.
     *
     * @throws Exception the Exception occurred during run if any
     */
    public void finish() throws Exception {
        if (exception != null) {
            throw exception;
        }
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
