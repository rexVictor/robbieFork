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

/**
 * Tests the ExceptionConstructorTest class.
 */
public class ExceptionConstructorTestTest {

    private static class ThrowableConstructorException extends Exception {

        public ThrowableConstructorException(Throwable cause) {
           super(cause);
        }

    }

    private static class WrongCauseException extends Exception {

        public WrongCauseException(Throwable cause) {
            super(new NoThrowableConstructorException());
        }
    }

    private static class NoThrowableConstructorException extends Exception {

        public NoThrowableConstructorException() {
            super();
        }
    }

    private static class ExceptionThrowingException extends Exception {

        public ExceptionThrowingException() {
            throw new NullPointerException();
        }
    }

    private static class WrongMessageException extends Exception {

        public WrongMessageException() {
            super("Wrong message");
        }
    }

    /**
     * Empty constructor.
     */
    public ExceptionConstructorTestTest(){
    }

    @Test
    public void isException_true() {
        Assert.assertTrue(ExceptionConstructorTest.isException(Exception.class));
    }

    @Test
    public void isException_false() {
        Assert.assertFalse(ExceptionConstructorTest.isException(Object.class));
    }

    @Test
    public void hasEmptyConstructor_true() {
        Assert.assertTrue(ExceptionConstructorTest.hasEmptyConstructor(Exception.class));
    }

    @Test
    public void hasEmptyConstructor_false() {
        Assert.assertFalse(ExceptionConstructorTest.hasEmptyConstructor(ThrowableConstructorException.class));
    }

    @Test
    public void hasEmptyConstructor_false_ExceptionThrown() {
        Assert.assertFalse(ExceptionConstructorTest.hasEmptyConstructor(ExceptionThrowingException.class));
    }

    @Test
    public void hasStringConstructor_true() {
        Assert.assertTrue(ExceptionConstructorTest.hasStringConstructor(Exception.class));
    }

    @Test
    public void hasStringConstructor_false() {
        Assert.assertFalse(ExceptionConstructorTest.hasStringConstructor(ThrowableConstructorException.class));
    }

    @Test
    public void hasStringThrowableConstructor_true() {
        Assert.assertTrue(ExceptionConstructorTest.hasStringThrowableConstructor(Exception.class));
    }

    @Test
    public void hasStringThrowableConstructor_false() {
        Assert.assertFalse(ExceptionConstructorTest.hasStringThrowableConstructor(ThrowableConstructorException.class));
    }

    @Test
    public void hasThrowableConstructor_true() {
        Assert.assertTrue(ExceptionConstructorTest.hasThrowableConstructor(Exception.class));
    }

    @Test
    public void hasThrowableConstructor_false() {
        Assert.assertFalse(ExceptionConstructorTest.hasThrowableConstructor(NoThrowableConstructorException.class));
    }

    @Test
    public void hasThrowableConstructor_false_WrongCause() {
        Assert.assertFalse(ExceptionConstructorTest.hasThrowableConstructor(WrongCauseException.class));
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */