package de.leifaktor.robbie.controllers.clock;

import de.leifaktor.robbie.api.controllers.clock.Clock;
import de.leifaktor.robbie.api.controllers.clock.ClockException;
import de.leifaktor.robbie.api.controllers.clock.ClockListener;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;
import org.apache.tools.ant.taskdefs.Exec;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Tests the ClockExceptionHandler.
 */
public class ClockExceptionHandlerTest {

    private static class ClockStub implements Clock {

        @Override
        public void addClockListener(ClockListener listener) {

        }

        @Override
        public void setTickDuration(long duration, TimeUnit timeUnit) {

        }

        @Override
        public void startClock() {

        }

        @Override
        public void stopClock() throws ClockException {

        }

        @Override
        public boolean state() throws ClockException {
            return false;
        }

        @Override
        public long getTickDurationInMillis() {
            return 0;
        }
    }

    /**
     * The ExecutorService the tests use.
     */
    private ExecutorService service;

    /**
     * The clock the tests use.
     */
    private Clock clock;

    /**
     * Helper instance used to indicate if something got called.
     */
    private AtomicBoolean gotCalled;

    /**
     * Helper instance to lock for Multithreading.
     */
    private Lock lock;

    /**
     * A condition of lock.
     */
    private Condition condition;

    /**
     * Empty Constructor.
     */
    public ClockExceptionHandlerTest() {
    }

    @BeforeMethod
    public void initializeInstanceVariables() {
        service = Executors.newCachedThreadPool();
        clock = new ClockStub();
        gotCalled = new AtomicBoolean(false);
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    @AfterMethod
    public void tearDownService() {
        if (service != null) {
            service.shutdownNow();
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public void new_ServiceNull() {
        service = null;
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_ClockNull() {
        Clock clock = null;
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void new_RestorerNull() {
        ClockRestorer restorer = null;
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
    }

    @Test(timeOut = 1000L)
    public void run_stopsOnInterruption() throws InterruptedException {
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);
        thread.start();
        thread.interrupt();
        thread.join();
    }

    @Test(timeOut = 1000L)
    public void run_StopsOnSleepingInterruption() throws InterruptedException {
        ClockRestorer restorer = cl -> { };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }
        thread.interrupt();
        thread.join();
    }

    @Test(timeOut = 1000L)
    public void run_notifiesRestorer_IfFirstExceptionOccurred() throws InterruptedException {
        ClockRestorer restorer = cl -> {
            lock.lock();
            try {
                gotCalled.set(true);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);

        handler.exceptionOccurred();
        thread.start();
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }

        Assert.assertTrue(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void run_notifiesRestorer_IfFirstRun() throws InterruptedException {
        ClockRestorer restorer = cl -> {
            lock.lock();
            try {
                gotCalled.set(true);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);
        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }

        handler.exceptionOccurred();
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }

        Assert.assertTrue(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void run_notRun_ifShutdown() throws InterruptedException {
        ClockRestorer restorer = cl -> { gotCalled.set(true); };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);

        handler.shutdown();
        Thread thread = new Thread(handler);
        thread.start();
        thread.join();

        Assert.assertFalse(gotCalled.get());

    }

    @Test(timeOut = 1000L)
    public void shutdown() throws InterruptedException {
        ClockRestorer restorer = cl -> { gotCalled.set(true); };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);

        handler.shutdown();
        Thread thread = new Thread(handler);
        thread.start();
        thread.join();

        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());

    }

    @Test(timeOut = 1000L)
    public void run_terminatesOnShutdownWithoutCalling() throws InterruptedException {
        ClockRestorer restorer = cl -> { gotCalled.set(true); };
        ClockExceptionHandler handler = new ClockExceptionHandler(service, clock, restorer);

        Thread thread = new Thread(handler);
        thread.start();
        while (thread.getState() != Thread.State.WAITING) {
            TimeUnit.MILLISECONDS.sleep(1L);
        }

        handler.shutdown();

        thread.join();

        Assert.assertFalse(gotCalled.get());
        Assert.assertTrue(service.isShutdown());
        Assert.assertTrue(service.isTerminated());

    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */