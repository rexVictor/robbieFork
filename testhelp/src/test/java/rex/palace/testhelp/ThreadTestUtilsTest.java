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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Tests the ThreadTestUtils class.
 */
public class ThreadTestUtilsTest {

    /**
     * Empty Constructor.
     */
    public ThreadTestUtilsTest() {
    }

    @Test
    public void assertUtilityClass() {
        Assert.assertTrue(UtilityCheck.isUtilityClass(ThreadTestUtils.class));
    }

    @Test(timeOut = 100L)
    public void waitTillThreadInState_new() throws InterruptedException {
        Thread thread = new Thread();
        ThreadTestUtils.waitTillThreadInState(thread, Thread.State.NEW);
    }

    @Test(timeOut = 1000L)
    public void waitTillThreadInState_timedWaiting() throws InterruptedException {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    //excepted
                }
            }
        };

        thread.start();
        ThreadTestUtils.waitTillThreadInState(thread, Thread.State.TIMED_WAITING);
    }

    @Test(timeOut = 1000L)
    public void waitTillThreadInState_blocked() throws InterruptedException {
        Thread thread = new Thread() {

            @Override
            public void run() {
                synchronized (ThreadTestUtilsTest.class) { }
            }

        };
        synchronized (ThreadTestUtilsTest.class) {
            thread.start();
            ThreadTestUtils.waitTillThreadInState(thread, Thread.State.BLOCKED);
        }
    }

    @Test(timeOut = 1000L)
    public void waitTillThreadInState_sleeping() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread thread = new Thread() {

            @Override
            public void run() {
                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    //can't happen
                } finally {
                    lock.unlock();
                }
            }

        };
        thread.start();
        ThreadTestUtils.waitTillThreadInState(thread, Thread.State.WAITING);

    }


}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
