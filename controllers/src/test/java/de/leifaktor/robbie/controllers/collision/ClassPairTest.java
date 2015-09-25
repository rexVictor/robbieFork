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

package de.leifaktor.robbie.controllers.collision;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the ClassPair class.
 */
public class ClassPairTest {

    /**
     * Empty constructor.
     */
    public ClassPairTest() {
        super();
    }

    @DataProvider(name = "classPairs")
    public Object[][] provideClassPairs() {
        return new Object[][]{
                {String.class, Number.class},
                {Integer.class, Integer.class},
                {Double.class, Exception. class},
                {IllegalArgumentException.class, RuntimeException.class}
        };
    }

    @DataProvider(name = "classPairsAndObjects")
    public Object[][] provideClassPairsAndObjects() {
        return new Object[][]{
                {String.class, Number.class, new Object()},
                {Integer.class, Integer.class, ""},
                {Double.class, Exception. class, 5},
                {IllegalArgumentException.class, RuntimeException.class, new StringBuilder()}
        };
    }

    @DataProvider(name = "classPairsUnequal")
    public Object[][] provideUnequalClassPairs() {
        return new Object[][]{
                {String.class, Number.class, String.class, Double.class},
                {Integer.class, Integer.class, Exception.class, Exception.class},
                {String.class, Number.class, StringBuilder.class, Number.class},
                {Exception.class, Float.class, String.class, Byte.class},
                {RuntimeException.class, Integer.class, Integer.class, Float.class},
                {System.class, Runtime.class, Float.class, System.class},
                {Long.class, Long.class, Double.class, Long.class},
                {Double.class, Long.class, Long.class, Long.class},
        };
    }

    @DataProvider(name = "classes")
    public Object[][] provideClasses() {
        return new Object[][]{
                {String.class}, {Number.class}, {Double.class}, {Integer.class}
        };
    }

    @Test(dataProvider = "classes", expectedExceptions = NullPointerException.class)
    public void new_firstNull(Class<?> clazz) {
        new ClassPair<>(null, clazz);
    }

    @Test(dataProvider = "classes", expectedExceptions = NullPointerException.class)
    public void new_secondNull(Class<?> clazz) {
        new ClassPair<>(clazz, null);
    }

    @Test(dataProvider = "classPairs")
    public void equals_swapped(Class<?> clazz1, Class<?> clazz2) {
        ClassPair<?, ?, ?> pair = new ClassPair<>(clazz1, clazz2);
        ClassPair<?, ?, ?> swapped = new ClassPair<>(clazz2, clazz1);

        Assert.assertEquals(swapped, pair);
        Assert.assertEquals(swapped.hashCode(), pair.hashCode());
    }

    @Test(dataProvider = "classPairs")
    public void equals_null(Class<?> clazz1, Class<?> clazz2) {
        ClassPair<?, ?, ?> pair = new ClassPair<>(clazz1, clazz2);

        Assert.assertFalse(pair.equals(null));
    }

    @Test(dataProvider = "classPairs")
    public void equals_same(Class<?> clazz1, Class<?> clazz2) {
        ClassPair<?, ?, ?> pair = new ClassPair<>(clazz1, clazz2);

        Assert.assertEquals(pair, pair);
        Assert.assertEquals(pair.hashCode(), pair.hashCode());
    }

    @Test(dataProvider = "classPairsAndObjects")
    public void equals_differentTypes(Class<?> clazz1, Class<?> clazz2, Object object) {
        ClassPair<?, ?, ?> pair = new ClassPair<>(clazz1, clazz2);

        Assert.assertNotEquals(pair, object);
        Assert.assertNotEquals(object, pair);
    }

    @Test(dataProvider = "classPairs")
    public void equals_equal(Class<?> clazz1, Class<?> clazz2) {
        ClassPair<?, ?, ?> pair = new ClassPair<>(clazz1, clazz2);
        ClassPair<?, ?, ?> equal = new ClassPair<>(clazz1, clazz2);

        Assert.assertEquals(equal, pair);
        Assert.assertEquals(equal.hashCode(), pair.hashCode());
    }

    @Test(dataProvider = "classPairsUnequal")
    public void equals_unequal(Class<?> clazz1, Class<?> clazz2,
                               Class<?> clazz3, Class<?> clazz4) {
        ClassPair<?, ?, ?> pair1 = new ClassPair<>(clazz1, clazz2);
        ClassPair<?, ?, ?> pair2 = new ClassPair<>(clazz3, clazz4);

        Assert.assertNotEquals(pair1, pair2);
        //The following assertion might fail.
        //If it does remove it.
        //hashCode() requires to return the same values for equal objects
        //but not to return different values for unequal objects.
        //But for performance reasons there shouldn't be much
        //collisions.
        Assert.assertNotEquals(pair1.hashCode(), pair2.hashCode());
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
