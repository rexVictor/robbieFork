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

//import de.leifaktor.robbie.api.controllers.Clock;
import de.leifaktor.robbie.api.controllers.ClockListener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

import rex.palace.testes.SequentialExecutorService;

import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Tests the TickEventHandler impementation.
 */
public class TickEventHandlerTest {

    /**
     * The Logger for this test.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TickEventHandlerTest.class);

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
        public CallCounter() {
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

    @Test(enabled = true)
    public void run_NoExceptions() throws InterruptedException {
        Set<ClockListener> listeners = new HashSet<>();
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        CallCounter callCounter = new CallCounter();
        listeners.add(callCounter);
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);

        for (int i = 0; i < 10; i++) {
            tickEventHandler.run();
        }
        Assert.assertEquals(callCounter.count, 10);
    }

    @Test
    public void run_interrupted_doesNothing() throws InterruptedException {
        Set<ClockListener> listeners = new HashSet<>();
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        CallCounter callCounter = new CallCounter();
        listeners.add(callCounter);
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Thread thread = new Thread() {

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    tickEventHandler.run();
                }
                interrupt();
                for (int i = 0; i < 5; i++) {
                    tickEventHandler.run();
                }
            }

        };
        thread.start();
        thread.join();

        Assert.assertEquals(callCounter.count, 5);
    }

    @Test
    public void shutdown() throws InterruptedException, TimeoutException {
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        Set<ClockListener> listeners = new HashSet<>();
        listeners.add(() -> { });
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());

        //Parameters don't matter for this test, since nothing runs parallel
        boolean correctShutdown = tickEventHandler.shutdown(1L, TimeUnit.MILLISECONDS);

        Assert.assertTrue(correctShutdown);
        Assert.assertTrue(seqExSer.isShutdown());
    }

    @Test(expectedExceptions = InterruptedException.class)
    public void shutdown_interrupted() throws TimeoutException, InterruptedException {
        Set<ClockListener> listeners = new HashSet<>();
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());
        seqExSer.submitForTerminationInTime(() -> { throw new InterruptedException(); } );

        boolean correctShutdown = true;
        try {
            //Paramets don't matter for this test, since nothing runs parallel.
            correctShutdown = tickEventHandler.shutdown(1L, TimeUnit.MILLISECONDS);
            Assert.fail("No InterruptedException got thrown.");
        } catch (InterruptedException e ) {
            Assert.assertTrue(seqExSer.isShutdown());
            throw e;
        }

    }

    @Test(expectedExceptions = TimeoutException.class)
    public void shutdown_tasksLeft() throws InterruptedException, TimeoutException {
        Set<ClockListener> listeners = new HashSet<>();
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());
        seqExSer.submitForNotFishingOnTermination(() -> null );

        boolean correctShutdown = true;
        try {
            //Paramets don't matter for this test, since nothing runs parallel.
            correctShutdown = tickEventHandler.shutdown(1L, TimeUnit.MILLISECONDS);
            Assert.fail("No TimeoutException got thrown.");
        } catch (TimeoutException e) {
            Assert.assertTrue(seqExSer.isShutdown());
            throw e;
        }
    }

    @Test
    public void areDone_false() {
        Set<ClockListener> listeners = new HashSet<>();
        listeners.add( () -> { } );
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        seqExSer.setState(SequentialExecutorService.ExecutorServiceState.NEVER);
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());

        tickEventHandler.run();

        Assert.assertFalse(tickEventHandler.areDone());

    }

    @Test
    public void areDone_true() {
        Set<ClockListener> listeners = new HashSet<>();
        listeners.add( () -> { } );
        SequentialExecutorService seqExSer = new SequentialExecutorService();
        TickEventHandler tickEventHandler = new TickEventHandlerImpl(
                seqExSer, listeners);
        Assert.assertFalse(seqExSer.isShutdown());

        tickEventHandler.run();

        Assert.assertTrue(tickEventHandler.areDone());

    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
