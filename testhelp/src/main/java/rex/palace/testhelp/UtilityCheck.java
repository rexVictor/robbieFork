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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Checks if a class is a utility class.
 */
public final class UtilityCheck {

    /**
     * Empty private constructor, since this is a Utility class.
     */
    private UtilityCheck() {
    }

    /**
     * Returns true if all Methods of clazz are static.
     *
     * @param clazz the class to check
     * @return true if and only if all declared methods are static
     */
    public static boolean areMethodsStatic(Class<?> clazz) {
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        return methods.stream().allMatch(UtilityCheck::isMethodStatic);
    }

    /**
     * Returns true if method is static.
     *
     * @param method the method to check
     * @return true if and only if method is static
     */
    public static boolean isMethodStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Returns true if clazz is final.
     *
     * @param clazz the class to check
     * @return true if and only if clazz is final
     */
    public static boolean isClassFinal(Class<?> clazz) {
        return Modifier.isFinal(clazz.getModifiers());
    }

    /**
     * Returns if clazz has only one constructor.
     *
     * @param clazz the clazz to check
     * @return true if and only if clazz has one declared constructor
     */
    public static boolean hasOneConstructor(Class<?> clazz) {
        return clazz.getDeclaredConstructors().length == 1;
    }

    /**
     * Returns if constructor is private.
     *
     * @param constructor the constructor to check
     * @return true if and only if constructor is private
     */
    public static boolean isConstructorPrivate(Constructor<?> constructor) {
        return Modifier.isPrivate(constructor.getModifiers());
    }

    /**
     * Returns if the constructors of clazz meets the utililty class design.
     *
     * <p>This means clazz may have only one private no argument constructor.
     *
     * @param clazz the class to check
     * @return true if and only if clazz has only one constructor and this
     *         constructor is private and has no arguments and it can be invoked
     */
    public static boolean checkConstructors(Class<?> clazz) {
        if (!hasOneConstructor(clazz)) {
            return false;
        }
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        if (!isConstructorPrivate(constructor)) {
            return false;
        }
        return canConstruct(constructor);
    }

    /**
     * Returns true if constructor can be invoked without
     * problems without arguments.
     *
     * @param constructor the constructor to invoke
     * @return true if and only if constructor has no arguments and
     *         instantiation can be done
     */
    public static boolean canConstruct(Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            constructor.newInstance();
            return true;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            //expected
            return false;
        }
    }

    /**
     * Returns if clazz meets the utility class design.
     *
     * @param clazz the class to check
     * @return if and only if clazz meets the utility class design except
     *         it must not be final
     */
    public static boolean isNonFinalUtilityClass(Class<?> clazz) {
        return checkConstructors(clazz) && areMethodsStatic(clazz);
    }

    /**
     * Returns if clazz meets the utility class design.
     *
     * @param clazz the class to check
     * @return if and only if clazz meets the utility class design and
     *         is final
     */
    public static boolean isUtilityClass(Class<?> clazz) {
        return isClassFinal(clazz) && isNonFinalUtilityClass(clazz);
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
