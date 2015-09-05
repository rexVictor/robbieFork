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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Tests if a given class is an Exception and provides the same Constructors as Exception.
 */
public final class ExceptionConstructorTest {

    /**
     * Empty Private Constructor since this is a Utility class.
     */
    private ExceptionConstructorTest() {
    }

    /**
     * Returns if clazz is a subclass of Exception.
     *
     * @param clazz the clazz to check
     * @return true if and only if clazz represents a subtype of Exception
     */
    public static boolean isException(Class<?> clazz) {
        return Exception.class.isAssignableFrom(clazz);
    }

    /**
     * Returns if clazz has an empty constructor and on invoking cause and message
     * are null.
     *
     * @param <V> the type of the exception
     * @param clazz the class to check
     * @return if clazz has an empty constructor which sets message and cause to null
     */
    public static <V extends Exception> boolean hasEmptyConstructor(Class<V> clazz) {
        Class<?>[] types = new Class<?>[] { };
        Exception exception = getException(clazz, types);
        if (exception == null) {
            return false;
        }
        return correctData(exception, null, null);
    }

    /**
     * Returns if clazz has a String constructor and on invoking cause is null and message
     * is set properly.
     *
     * @param <V> the type of the exception
     * @param clazz the class to check
     * @return if clazz has an empty constructor which sets message properly and cause to null
     */
    public static <V extends Exception> boolean hasStringConstructor(Class<V> clazz) {
        Class<?>[] types = new Class<?>[] { String.class };
        String testString = "kdrtunadirtbples";
        Exception exception = getException(clazz, types, testString);
        if (exception == null) {
            return false;
        }
        return correctData(exception, testString, null);
    }

    /**
     * Returns if clazz has a String, Throwable constructor and on invoking message
     * and case are set properly.
     *
     * @param <V> the type of the exception
     * @param clazz the class to check
     * @return if clazz has a String, Throwable constructor which sets properly.
     */
    public static <V extends Exception> boolean hasStringThrowableConstructor(Class<V> clazz) {
        Class<?>[] types = new Class<?>[] { String.class, Throwable.class };
        String testString = "doseairbpypmvlsugyae1902h23etiarrtä";
        Throwable testCause = new OutOfMemoryError();
        Exception exception = getException(clazz, types, testString, testCause);
        if (exception == null) {
            return false;
        }
        return correctData(exception, testString,testCause);
    }

    /**
     * Returns an Exception instance of type clazz.
     *
     * <p>It is constructed using the Constructor with types and argument values.
     *
     * @param <V> the type of the exception
     * @param clazz the class to instantiate
     * @param types the constructor to use
     * @param values the parameters for construction
     * @return an instance of V constructed using the types constructor
     *         and values as its parameters or null if it could not be done
     */
    private static <V extends Exception> V getException(
            Class<V> clazz, Class<?>[] types, Object... values) {
        Constructor<V> constructor = getConstructor(clazz, types);
        return construct(constructor, values);
    }

    /**
     * Returns a Constructor of V with paramTypes as its parameter types.
     *
     * @param <V> the type of the Exception
     * @param clazz the clazz a Constructor shall be gotten
     * @param paramTypes the parameter types the constructor should have
     * @return an constructor of type V wih paramTypes as its arguments or
     *         null if such a constructor could not be found
     */
    private static <V extends Exception> Constructor<V> getConstructor(
            Class<V> clazz, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            //expected
            return null;
        }
    }

    /**
     * Constructs an Exception using constructor and args.
     *
     * @param constructor the constructor to use for construction
     * @param args the arguments for constructor
     * @param <V> the type of the Exception
     * @return an instance of v constructed using constructor with args
     *         or null if constructor is null or if the instance could
     *         not be created
     */
    private static <V extends Exception> V construct(Constructor<V> constructor,
                                       Object... args) {
        if (constructor == null) {
            return null;
        }
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //expected
            return null;
        }
    }

    /**
     * Returns if clazz has a Throwable constructor and on invoking message is
     * set to cause.toString() and cause properly.
     *
     * @param <V> the type of the exception
     * @param clazz the class to check
     * @return true if and only if clazz has a Throwable constructor, which
     *         respects the API of Exception(Throwable)
     */
    public static <V extends Exception> boolean hasThrowableConstructor(Class<V> clazz) {
        Class<?>[] types = new Class<?>[] { Throwable.class };
        Throwable testCause = new ArrayIndexOutOfBoundsException();
        Exception exception = getException(clazz, types, testCause);
        if (exception == null) {
            return false;
        }
        return correctData(exception, testCause.toString(), testCause);
    }

    /**
     * Returns if the message and the cause of exception satisfy the parameters.
     *
     * @param exception the exception to check
     * @param message the message to check against
     * @param cause the cause to check against
     * @return if the message and the cause of the exception are equal to the parameters
     */
    private static boolean correctData(Exception exception, String message, Throwable cause) {
        if (cause != exception.getCause()) {
            return false;
        }
        if (message != null) {
            return message.equals(exception.getMessage());
        }
        return exception.getMessage() == null;
    }

    /**
     * Returns if clazz has all Exception constructors and each sets message and cause
     * properly.
     *
     * @param <V> the type of the exception
     * @param clazz the class to check
     * @return if clazz describes a proper exception
     */
    public static <V extends Exception> boolean hasAllConstructors(Class<V> clazz) {
        return hasEmptyConstructor(clazz) && hasStringConstructor(clazz)
                && hasStringThrowableConstructor(clazz) && hasThrowableConstructor(clazz);
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */