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

import de.leifaktor.robbie.api.controllers.clock.ClockException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * Tests static methods of ClockImpl.
 */
public class ClockImplTest {

    /**
     * Empty constructor.
     */
    public ClockImplTest(){
    }

    @DataProvider(name = "runtimeExceptions")
    public Object[][] getRuntimeExceptions() {
        return new Object[][]{
                { new NullPointerException() }, { new IllegalArgumentException() },
                { new ArrayIndexOutOfBoundsException() }, { new RuntimeException() },
                { new NumberFormatException() }, { new ClassCastException() }
        };
    }

    @DataProvider(name = "checkedExceptions")
    public Object[][] getCheckedExceptions() {
        return new Object[][] {
                { new CloneNotSupportedException() }, { new FileNotFoundException() },
                { new Exception() }, { new IOException() }, { new MalformedURLException() },
                { new SQLException() }, {new InterruptedException() },
                { new NoSuchMethodException() }, { new ClassNotFoundException() }
        };
    }

    @Test(dataProvider = "checkedExceptions")
    public void wrapToClockException_CheckedException(Exception checkedExcep) {
        ExecutionException execExcep = new ExecutionException(checkedExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);
        Throwable cause = wrapped.getCause();

        Assert.assertSame(cause, checkedExcep);
    }

    @Test
    public void wrapToClockException_ClockException() {
        ClockException clockExcep = new ClockException();
        ExecutionException execExcep = new ExecutionException(clockExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);

        Assert.assertSame(wrapped, clockExcep);
    }

    @Test
    public void wrapToClockException_ClockRuntimeException() {
        ClockException clockExcep = new ClockException();
        ClockRuntimeException clockRunExcep = new ClockRuntimeException(clockExcep);
        ExecutionException execExcep = new ExecutionException(clockRunExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);

        Assert.assertSame(wrapped, clockExcep);
    }

    @Test(dataProvider = "runtimeExceptions")
    public void wrapToClockException_RuntimeException(RuntimeException runExcep) {
        ExecutionException execExcep = new ExecutionException(runExcep);

        ClockException wrapped = ClockImpl.wrapToClockException(execExcep);
        Throwable cause = wrapped.getCause();

        Assert.assertSame(cause, runExcep);
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
