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

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Tests the ClockListenerThreadFactory.
 */
public class ClockListenerThreadFactoryTest {

    /**
     * A dash (-) sign.
     */
    private static final String DASH = "-";

    /**
     * A mock implementation of UncaughtExceptionHandler.
     */
    private static class UncaughtExceptionHandlerMock implements Thread.UncaughtExceptionHandler {

        /**
         * The last uncaught exception thrown by a Thread using this handler.
         */
        public Throwable uncaughtException;

        /**
         * The last Thread using this handler, which threw an Exception.
         */
        public Thread throwingThread;

        /**
         * Creates a new UncaughtExceptionHandlerMock.
         */
        UncaughtExceptionHandlerMock() {
            super();
        }

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {
            throwingThread = thread;
            uncaughtException = exception;
        }

    }

    /**
     * A stub implementation of Runnable.
     *
     * <p>Its run methods throws a RuntimeException if it is set accordingly.
     */
    private static class RunnableStub implements Runnable {

        /**
         * The result of toString().
         */
        public String name;

        /**
         * The RuntimeException run() shall throw.
         */
        public RuntimeException exception;

        /**
         * Creates a new RunnableStub.
         */
        RunnableStub() {
            super();
        }

        @Override
        public void run() {
            if (exception != null) {
                throw exception;
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * A UncaughtExceptionHandlerMock instance tests can use.
     */
    private UncaughtExceptionHandlerMock handler;

    /**
     * The ClockListenerThreadFactory to be tested.
     */
    private ClockListenerThreadFactory threadFactory;

    /**
     * A RunnableStub instance tests can use.
     */
    private RunnableStub runnableStub;

    /**
     * Empty Constructor.
     */
    public ClockListenerThreadFactoryTest() {
        super();
    }

    /**
     * Initializes instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariables() {
        handler = new UncaughtExceptionHandlerMock();
        threadFactory = new ClockListenerThreadFactory(handler);
        runnableStub = new RunnableStub();
    }

    @DataProvider(name = "threadCountsCrossRunnables")
    public Iterator<Object[]> getThreadCountsCrossRunnables() {
        UncaughtExceptionHandlerMock handlerMock = new UncaughtExceptionHandlerMock();
        List<Object[]> dataList = new ArrayList<>();
        for (int instanceCount = 0; instanceCount <= 2; instanceCount++) {
            for (int threadCount = 0; threadCount <= 4; threadCount++) {
                for (int taskCount = 0; taskCount <= 2; taskCount++) {
                    ClockListenerThreadFactory factory
                            = new ClockListenerThreadFactory(
                            handlerMock, instanceCount);
                    for (int i = 0; i < threadCount; i++) {
                        factory.newThread(runnableStub);
                    }
                    RunnableStub stub = new RunnableStub();
                    stub.name = String.valueOf(taskCount);
                    dataList.add(new Object[] {
                            factory, instanceCount, threadCount, stub
                    });
                }
            }
        }
        return dataList.iterator();
    }

    @Test(dataProvider = "threadCountsCrossRunnables")
    public void correctName(
            ThreadFactory initializedThreadFactory, int instanceCount,
            int threadCount, RunnableStub task) {
        Thread thread = initializedThreadFactory.newThread(task);
        String expectedName = expectedName(instanceCount, threadCount, task);
        Assert.assertEquals(thread.getName(), expectedName);
    }

    /**
     * Returns the expected name of a Thread.
     *
     * @param instanceCount the first number in the String
     * @param threadCount the second number in the String
     * @param runnable the runnable the Thread is executing
     * @return the expected String of Thread.getName();
     */
    private String expectedName(int instanceCount, int threadCount, Runnable runnable) {
        return "ClockListenerThread-" + instanceCount + DASH
                + threadCount + DASH + runnable.toString();
    }

    @Test
    public void isDaemon() {
        Thread thread = threadFactory.newThread(runnableStub);

        Assert.assertTrue(thread.isDaemon());
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void handlerIsCalled() throws Throwable {
        runnableStub.exception = new ClassCastException();
        Thread thread = threadFactory.newThread(runnableStub);
        thread.start();
        thread.join();

        Assert.assertSame(handler.throwingThread, thread);

        throw handler.uncaughtException;
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
