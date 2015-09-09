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

package de.leifaktor.robbie.controllers.clock;

import de.leifaktor.robbie.api.controllers.clock.ClockListener;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import rex.palace.testes.ExecutorServiceState;
import rex.palace.testes.SequentialExecutorService;
import rex.palace.testhelp.TestThread;

import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Tests the TickEventHandler implementation.
 */
public class TickEventHandlerTest {

    /**
     * A SequentialExecutorService, which gets set to a new one
     * before every test.
     */
    private SequentialExecutorService seqExSer;

    /**
     * The callCounter to use for testing.
     */
    private CallCounter callCounter;

    /**
     * A TickEventHandler only calling callCounter.
     */
    private TickEventHandler callCounterHandler;

    /**
     * A TickEventHandler calling nothing.
     */
    private TickEventHandler callNothingHandler;

    /**
     * A TickEventHandler calling the empty lambda.
     */
    private TickEventHandler callEmptyHandler;

    /**
     * Simple ClockListener implementation, that counts the calls
     * to ticksPassed().
     */
    private static class CallCounter implements ClockListener {

        /**
         * The number of calls to ticksPassed().
         */
        public int count = 0;

        /**
         * Empty constructor.
         */
        CallCounter() {
        }

        @Override
        public void ticksPassed() {
            count++;
        }

    }


    /**
     * Empty Constructor.
     */
    public TickEventHandlerTest() {
    }

    /**
     * Initializes instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariables() {
        seqExSer = new SequentialExecutorService();
        callCounter = new CallCounter();
        callNothingHandler = new TickEventHandlerImpl(
                seqExSer, new HashSet<>());
        Assert.assertFalse(seqExSer.isShutdown());
        Set<ClockListener> listeners = new HashSet<>();
        listeners.add(callCounter);
        callCounterHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());
        Set<ClockListener> emptyLambdaSet = new HashSet<>();
        emptyLambdaSet.add( () -> { });
        callEmptyHandler = new TickEventHandlerImpl(
                seqExSer, emptyLambdaSet);
        Assert.assertFalse(seqExSer.isShutdown());
    }


    @Test(enabled = true)
    public void run_NoExceptions() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            callCounterHandler.run();
        }
        Assert.assertEquals(callCounter.count, 10);
    }

    @Test
    public void run_interrupted_doesNothing() throws InterruptedException {
        Thread thread = new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    callCounterHandler.run();
                }
                interrupt();
                for (int i = 0; i < 5; i++) {
                    callCounterHandler.run();
                }
            }

        };
        thread.start();
        thread.join();

        Assert.assertEquals(callCounter.count, 5);
    }

    @Test
    public void shutdown() throws InterruptedException, TimeoutException {
        //Parameters don't matter for this test, since nothing runs parallel
        boolean correctShutdown = callEmptyHandler.shutdown(1L, TimeUnit.MILLISECONDS);

        Assert.assertTrue(correctShutdown);
        Assert.assertTrue(seqExSer.isShutdown());
    }

    @Test(expectedExceptions = InterruptedException.class)
    public void shutdown_interrupted() throws Exception {
        seqExSer.submitForTerminationInTime(() -> null);
        TestThread testThread = new TestThread(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                Thread.currentThread().interrupt();
                callNothingHandler.shutdown(1L, TimeUnit.MILLISECONDS);
                Assert.fail("No InterruptedException got thrown.");
                return null;
            }
        });

        testThread.start();
        testThread.join();
        Assert.assertTrue(seqExSer.isShutdown());
        testThread.finish();
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void shutdown_tasksLeft() throws InterruptedException, TimeoutException {
        seqExSer.submitForNotFishingOnTermination(() -> null );

        try {
            //Parameters don't matter for this test, since nothing runs parallel.
            callNothingHandler.shutdown(1L, TimeUnit.MILLISECONDS);
            Assert.fail("No TimeoutException got thrown.");
        } catch (TimeoutException e) {
            Assert.assertTrue(seqExSer.isShutdown());
            throw e;
        }
    }

    @Test
    public void areDone_false() {
        seqExSer.setState(ExecutorServiceState.NEVER);

        callEmptyHandler.run();

        Assert.assertFalse(callEmptyHandler.areDone());
    }

    @Test
    public void areDone_true() {
        callEmptyHandler.run();

        Assert.assertTrue(callEmptyHandler.areDone());
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
