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

package rex.palace.testes;

import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Tests the NeverDoneFuture class.
 */
public class NeverDoneFutureTest {

    /**
     * Empty constructor.
     */
    public NeverDoneFutureTest() {
    }

    @Test(expectedExceptions = ExecutionException.class)
    public void get() throws ExecutionException, InterruptedException {
        Future<Void> future = new NeverDoneFuture<>(() -> null);
        future.get();
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
