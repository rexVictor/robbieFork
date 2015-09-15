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

package rex.palace.testes.scheduled;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rex.palace.testhelp.ArgumentConverter;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.LongStream;

/**
 * Tests the DelayedPeriodicSequentialFuture class.
 */
public class DelayedPeriodicSequentialFutureTest {

    /**
     * A Callable which counts how often call() got called.
     */
    private static class CallCounter implements Callable<Void> {

        /**
         * How often call() got called.
         */
        public int callCount = 0;

        /**
         * The exception to throw.
         */
        public Exception exception = null;

        /**
         * Creates a new CallCounter.
         */
        CallCounter() {
            super();
        }

        /**
         * Throws exception if it is not null and increments callCount otherwise.
         * @return null
         * @throws Exception if exception is not null
         */
        @Override
        public Void call() throws Exception {
            if (exception != null) {
                throw exception;
            }
            callCount++;
            return null;
        }
    }

    /**
     * A mock implementation of TimeController.
     */
    private static class MockTimeController implements TimeController {

        /**
         * The TimeListener which registered last.
         */
        public TimeListener registered = null;

        /**
         * Creates a new MockTimeController.
         */
        MockTimeController() {
            super();
        }

        @Override
        public void letTimePass(long time, TimeUnit unit) {
            //does nothing
        }

        @Override
        public void register(TimeListener listener) {
            registered = listener;
        }

        @Override
        public void unregister(TimeListener listener) {
            //does nothing
        }
    }

    /**
     * The CallCounter this' tests can use.
     */
    private CallCounter callCounter;

    /**
     * The DelayedPeriodicSequentialFuture to be tested.
     */
    private DelayedPeriodicSequentialFuture<Void> future;

    /**
     * The default TimeController implementation used by future.
     */
    private TimeController timeController;

    /**
     * A MockTimeController.
     */
    private MockTimeController mockTimeController;

    /**
     * Empty constructor.
     */
    public DelayedPeriodicSequentialFutureTest() {
        super();
    }

    @DataProvider(name = "nonPositiveLongs")
    public Iterator<Object[]> getNonPositiveLongs() {
        return ArgumentConverter.convert(LongStream.range(0, 10).mapToObj(lg -> -lg));
    }

    /**
     * Initializes the instance variables.
     */
    @BeforeMethod
    public void initializeInstanceVariable() {
        callCounter = new CallCounter();
        timeController = new TimeControllerImpl();
        future = new DelayedPeriodicSequentialFuture<>(
                callCounter, 5L, 10L, TimeUnit.NANOSECONDS, timeController);
        mockTimeController = new MockTimeController();
    }

    /**
     * Makes initial assertions before the actual tests are run.
     */
    @BeforeMethod(dependsOnMethods = "initializeInstanceVariable")
    public void basicAssertions() {
        Assert.assertFalse(future.isCancelled());
        Assert.assertFalse(future.isDone());
        Assert.assertEquals(callCounter.callCount, 0);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullCallable() {
        new DelayedPeriodicSequentialFuture<>(
                null, 5L, 10L, TimeUnit.NANOSECONDS,
                NopTimeController.nopController);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullUnit() {
        new DelayedPeriodicSequentialFuture<>(
                callCounter, 5L,  10L, null, NopTimeController.nopController);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_nullController() {
        new DelayedPeriodicSequentialFuture<>(
                callCounter, 5L, 10L, TimeUnit.NANOSECONDS, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "nonPositiveLongs")
    public void new_NonPositiveDuration(long duration) {
        new DelayedPeriodicSequentialFuture<>(
                callCounter, duration, 10L, TimeUnit.NANOSECONDS,
                NopTimeController.nopController);
    }

    @Test
    public void new_registersAtTimeController() {
        TimeListener futureToRegister
                = new PeriodicSequentialFuture<>(
                callCounter, 10L, TimeUnit.NANOSECONDS, mockTimeController);

        Assert.assertSame(mockTimeController.registered, futureToRegister);
    }

    @Test
    public void normalRun() {
        timeController.letTimePass(4L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(callCounter.callCount, 0);
        timeController.letTimePass(1L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(callCounter.callCount, 1);
        timeController.letTimePass(90L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(callCounter.callCount, 10);
        Assert.assertFalse(future.isDone());
        Assert.assertFalse(future.isCancelled());
    }

    @Test
    public void exceptionalRun() {
        timeController.letTimePass(100L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(callCounter.callCount, 10);
        callCounter.exception = new FileNotFoundException();
        timeController.letTimePass(10L, TimeUnit.NANOSECONDS);

        Assert.assertEquals(callCounter.callCount, 10);
        Assert.assertTrue(future.isDone());
        Assert.assertFalse(future.isCancelled());

        timeController.letTimePass(100L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(callCounter.callCount, 10);

    }

    @Test
    public void cancel() {
        Assert.assertTrue(future.cancel(true));
        Assert.assertTrue(future.isCancelled());

        timeController.letTimePass(100L, TimeUnit.MILLISECONDS);

        Assert.assertEquals(callCounter.callCount, 0);

        Assert.assertFalse(future.cancel(true));
    }

    @Test
    public void cancel_AfterException() {
        callCounter.exception = new ClassCastException();
        timeController.letTimePass(10L, TimeUnit.NANOSECONDS);

        Assert.assertFalse(future.cancel(true));
    }

    @Test
    public void getDelay() {
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 5L);

        timeController.letTimePass(4L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 1L);

        timeController.letTimePass(1L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 10L);

        timeController.letTimePass(9L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 1L);

        timeController.letTimePass(11L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 10L);

        timeController.letTimePass(3L, TimeUnit.NANOSECONDS);
        Assert.assertEquals(future.getDelay(TimeUnit.NANOSECONDS), 7L);
    }

    @Test(expectedExceptions = MalformedURLException.class, timeOut = 1000L)
    public void get() throws Throwable {
        callCounter.exception = new MalformedURLException();
        try {
            future.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test(expectedExceptions = TimeoutException.class, timeOut = 1000L)
    public void get_TimeOut() throws InterruptedException, ExecutionException, TimeoutException {
        callCounter.exception = new MalformedURLException();
        future.get(4L, TimeUnit.NANOSECONDS);
    }

    @Test(expectedExceptions = MalformedURLException.class, timeOut = 1000L)
    public void get_limited() throws Throwable {
        callCounter.exception = new MalformedURLException();
        try {
            future.get(10L, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
