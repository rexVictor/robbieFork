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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests the TestThread class.
 */
public class TestThreadTest {

    /**
     * Empty Constructor.
     */
    public TestThreadTest(){
    }

    @Test(expectedExceptions = CloneNotSupportedException.class)
    public void finish_exceptionThrown() throws Throwable {
        TestThread testThread = new TestThread(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                throw new CloneNotSupportedException();
            }
        });

        testThread.start();
        testThread.join();
        testThread.finish();
    }

    @Test
    public void test_RunsCorrect() throws Throwable {

        AtomicBoolean atom = new AtomicBoolean(false);
        TestThread testThread = new TestThread(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                atom.set(true);
                return null;
            }
        });

        testThread.start();
        testThread.join();
        testThread.finish();
        Assert.assertTrue(atom.get());
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
