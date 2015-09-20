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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tests the ExceptionConstructorTest class.
 */
public class ExceptionConstructorTestTest {


    /**
     * An Exception which does have not the parameter as a cause.
     */
    private static class WrongCauseException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 19L;

        /**
         * Creates a new WrongCauseException which does not use its parameter
         * as cause.
         *
         * @param cause discarded
         */
        WrongCauseException(Throwable cause) {
            super(new EmptyException());
        }

    }

    /**
     * An Exception whose Constructor throws an Exception.
     */
    private static class ExceptionThrowingException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new ExceptionThrowingException.
         */
        ExceptionThrowingException() {
            throw new NullPointerException();
        }

    }

    /**
     * An Exception which does not have the correct message.
     */
    private static class WrongMessageException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 2L;

        /**
         * Creates a new WrongMessageException.
         *
         * @param message the message this exception will not have
         */
        WrongMessageException(String message) {
            super(message + " ");
        }

    }

    /**
     * An Exception witch has a message but should have none.
     */
    private static class ShouldHaveNoMessageException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 3L;

        /**
         * Creates a new ShouldHaveNoMessageException.
         */
        ShouldHaveNoMessageException() {
            super("Message");
        }
    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 4L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyException() {
            super();
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyStringException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 5L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyStringException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        EmptyStringException(String message) {
            super(message);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyThrowableException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 6L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyThrowableException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        EmptyThrowableException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 7L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyBothException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        EmptyBothException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyStringThrowableException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 8L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyStringThrowableException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        EmptyStringThrowableException(String message) {
            super(message);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        EmptyStringThrowableException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyStringBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 9L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyStringBothException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        EmptyStringBothException(String message) {
            super(message);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        EmptyStringBothException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * An Exception API conform extension.
     */
    private static class EmptyThrowableBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 10L;

        /**
         * Creates a new Exception API conform instance.
         */
        EmptyThrowableBothException() {
            super();
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        EmptyThrowableBothException(Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        EmptyThrowableBothException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * An Exception API conform extension.
     */
    private static class StringException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 11L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        StringException(String message) {
            super(message);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class StringThrowableException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 12L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        StringThrowableException(String message) {
            super(message);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        StringThrowableException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class StringBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 13L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        StringBothException(String message) {
            super(message);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        StringBothException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class StringThrowableBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 14L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         */
        StringThrowableBothException(String message) {
            super(message);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        StringThrowableBothException(Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        StringThrowableBothException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class ThrowableException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 15L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        ThrowableException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class ThrowableBothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 16L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param cause the cause of this exception
         */
        ThrowableBothException(Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        ThrowableBothException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class BothException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 17L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param message the message to use
         * @param cause the cause of this exception
         */
        BothException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * An Exception API conform extension.
     */
    private static class WeirdException extends Exception {

        /**
         * Needed for possible serialization.
         */
        private static final long serialVersionUID = 18L;

        /**
         * Creates a new Exception API conform instance.
         *
         * @param weirdParameter not used, just making weird
         */
        WeirdException(int weirdParameter) {
        }
    }

    /**
     * Empty constructor.
     */
    public ExceptionConstructorTestTest() {
    }

    @Test
    public void isUtilityClass() {
        UtilityCheck.isUtilityClass(ExceptionConstructorTest.class);
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
        Assert.assertFalse(ExceptionConstructorTest.hasEmptyConstructor(ThrowableException.class));
    }

    @Test
    public void hasEmptyConstructor_false_wrongMessage() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasEmptyConstructor(
                        ShouldHaveNoMessageException.class));
    }

    @Test
    public void hasEmptyConstructor_false_ExceptionThrown() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasEmptyConstructor(
                        ExceptionThrowingException.class));
    }

    @Test
    public void hasStringConstructor_true() {
        Assert.assertTrue(
                ExceptionConstructorTest.hasStringConstructor(
                        Exception.class));
    }

    @Test
    public void hasStringConstructor_false() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasStringConstructor(
                        ThrowableException.class));
    }

    @Test
    public void hasStringThrowableConstructor_true() {
        Assert.assertTrue(
                ExceptionConstructorTest.hasStringThrowableConstructor(
                        Exception.class));
    }

    @Test
    public void hasStringThrowableConstructor_false() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasStringThrowableConstructor(
                        ThrowableException.class));
    }

    @Test
    public void hasThrowableConstructor_true() {
        Assert.assertTrue(
                ExceptionConstructorTest.hasThrowableConstructor(
                        Exception.class));
    }

    @Test
    public void hasThrowableConstructor_false() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasThrowableConstructor(
                        EmptyException.class));
    }

    @Test
    public void hasThrowableConstructor_false_WrongCause() {
        Assert.assertFalse(
                ExceptionConstructorTest.hasThrowableConstructor(
                        WrongCauseException.class));
    }

    @Test
    public void hasAllConstructors_true() {
        Assert.assertTrue(
                ExceptionConstructorTest.hasAllConstructors(
                        Exception.class));
    }

    @Test(dataProvider = "notAllConstructors")
    public void hasAllConstructors_false(Class<? extends Exception> clazz) {
        Assert.assertFalse(ExceptionConstructorTest.hasAllConstructors(clazz));
    }

    @DataProvider(name = "notAllConstructors")
    public Iterator<Object[]> getExceptionsNotAllConstructors() {
        List<Object> classes = Arrays.asList(
                EmptyException.class, StringException.class,
                ThrowableException.class, BothException.class,
                EmptyStringException.class, EmptyThrowableException.class,
                EmptyBothException.class, StringThrowableException.class,
                StringBothException.class, ThrowableBothException.class,
                WeirdException.class, EmptyStringBothException.class,
                EmptyStringThrowableException.class, EmptyThrowableBothException.class,
                StringThrowableBothException.class);
        List<Object[]> wrapped = classes.stream().map(Collections::singleton)
                .map(Set::toArray).collect(Collectors.toList());
        return wrapped.iterator();
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */