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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tests the UtilityCheck class.
 */
public class UtilityCheckTest {

    /**
     * A class whose constructor throws an Exception.
     */
    private static class ConstructorThrows {

        /**
         * Creates a new ConstructorThrows.
         */
        ConstructorThrows() {
            throw new NullPointerException();
        }
    }

    /**
     * Empty Constructor.
     */
    public UtilityCheckTest() {
    }

    @Test
    public void isUtilityClass_true() {
        Assert.assertTrue(UtilityCheck.isUtilityClass(UtilityCheck.class));
    }

    @Test(dataProvider = "nonUtilityClasses")
    public void isUtilityClass_false(Class<?> clazz) {
        Assert.assertFalse(UtilityCheck.isUtilityClass(clazz));
    }

    @Test(dataProvider = "nonFinalUtilityClasses")
    public void isNonFinalUtilityClass_true(Class<?> clazz) {
        Assert.assertTrue(UtilityCheck.isNonFinalUtilityClass(clazz));
    }

    @DataProvider(name = "nonFinalUtilityClasses")
    public Iterator<Object[]> getNonFinalUtilityClasses() {
        List<Object> utilityClasses = Arrays.asList(Collections.class, Arrays.class);
        List<Object[]> wrapped = utilityClasses.stream().map(Collections::singleton)
                .map(Set::toArray).collect(Collectors.toList());
        return wrapped.iterator();
    }

    @Test(dataProvider = "nonUtilityClasses")
    public void isNonFinalUtilityClass_false(Class<?> clazz) {
        Assert.assertFalse(UtilityCheck.isNonFinalUtilityClass(clazz));
    }

    @DataProvider(name = "nonUtilityClasses")
    public Iterator<Object[]> getNonUtilityClasses() {
        List<Object> utilityClasses = Arrays.asList(
                String.class, Integer.class, getClass(), Runtime.class);
        List<Object[]> wrapped = utilityClasses.stream().map(Collections::singleton)
                .map(Set::toArray).collect(Collectors.toList());
        return wrapped.iterator();
    }

    @Test(dataProvider = "nonStaticMethods")
    public void isStaticMethod_false(Method method) {
        Assert.assertFalse(UtilityCheck.isMethodStatic(method));
    }

    @DataProvider(name = "nonStaticMethods")
    public Iterator<Object[]> getNonStaticMethods() throws NoSuchMethodException {
        List<Object> nonStaticMethods = new ArrayList<>();
        nonStaticMethods.add(String.class.getMethod("isEmpty"));
        nonStaticMethods.add(UtilityCheckTest.class.getMethod("getNonStaticMethods"));
        nonStaticMethods.add(Integer.class.getMethod("compareTo", Integer.class));
        nonStaticMethods.add(String.class.getMethod("charAt", int.class));
        List<Object[]> wrapped = nonStaticMethods.stream().map(Collections::singleton)
                .map(Set::toArray).collect(Collectors.toList());
        return wrapped.iterator();
    }

    @Test(dataProvider = "multipleConstructorClasses")
    public void hasOneConstructor_false(Class<?> clazz) {
        Assert.assertFalse(UtilityCheck.hasOneConstructor(clazz));
    }

    @Test(dataProvider = "multipleConstructorClasses")
    public void checkConstructors_MoreThanOne_false(Class<?> clazz) {
        Assert.assertFalse(UtilityCheck.checkConstructors(clazz));
    }

    @Test
    public void checkConstructors_publicOne_false() {
        Assert.assertFalse(UtilityCheck.checkConstructors(getClass()));
    }

    @Test
    public void canConstruct_Exception_false() throws NoSuchMethodException {
        Constructor<?> constructor = ConstructorThrows.class.getDeclaredConstructor();
        Assert.assertFalse(UtilityCheck.canConstruct(constructor));
    }

    @DataProvider(name = "multipleConstructorClasses")
    public Iterator<Object[]> getClassesWithMoreThanOneConstructor() {
        List<Object> multipleConstructors = Arrays.asList(String.class, Integer.class, File.class);
        List<Object[]> wrapped = multipleConstructors.stream().map(Collections::singleton)
                .map(Set::toArray).collect(Collectors.toList());
        return wrapped.iterator();
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
