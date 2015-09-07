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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests the ArgumentConverter class.
 */
public class ArgumentConverterTest {

    /**
     * Empty Constructor.
     */
    public ArgumentConverterTest(){
    }

    @Test
    public void assertUtilityClass() {
        UtilityCheck.isUtilityClass(ArgumentConverter.class);
    }

    @Test(dataProvider = "handCodedObjects")
    public void wrapInArrayHandCoded(Object object) {
        Object[] wrapped = ArgumentConverter.wrapInArray(object);

        Assert.assertEquals(wrapped.length, 1);
        Assert.assertSame(wrapped[0], object);
    }

    @DataProvider(name = "handCodedObjects")
    public Object[][] getHandCodedObjects() {
        return new Object[][] {
                { new Object() },
                { "Test" },
                { new StringBuilder() },
                { new ArgumentConverterTest() }
        };
    }

    @Test
    public void convert_array() {
        Object[] testArray =  new Object[]{
                new Object(), "TestString", new StringBuilder(), new ArgumentConverterTest()
        };
        Iterator<Object[]> toTestIterator = ArgumentConverter.convert(testArray);
        List<Object> assertList = new ArrayList<>(Arrays.asList(testArray));

        while (toTestIterator.hasNext()) {
            Object[] next = toTestIterator.next();
            Assert.assertTrue(assertList.remove(next[0]));
        }

        Assert.assertTrue(assertList.isEmpty());

    }

    @Test
    public void convert_list() {
        Object[] testArray =  new Object[]{
                new Object(), "TestString", new StringBuilder(), new ArgumentConverterTest()
        };
        List<Object> testList = Arrays.asList(testArray);
        Iterator<Object[]> toTestIterator = ArgumentConverter.convert(testList);
        List<Object> assertList = new ArrayList<>(testList);

        while (toTestIterator.hasNext()) {
            Object[] next = toTestIterator.next();
            Assert.assertTrue(assertList.remove(next[0]));
        }

        Assert.assertTrue(assertList.isEmpty());

    }

    @Test
    public void convert_stream() {
        Object[] testArray =  new Object[]{
                new Object(), "TestString", new StringBuilder(), new ArgumentConverterTest()
        };
        Stream<Object> testStream = Arrays.stream(testArray);
        Iterator<Object[]> toTestIterator = ArgumentConverter.convert(testStream);
        List<Object> assertList = new ArrayList<>(Arrays.asList(testArray));

        while (toTestIterator.hasNext()) {
            Object[] next = toTestIterator.next();
            Assert.assertTrue(assertList.remove(next[0]));
        }

        Assert.assertTrue(assertList.isEmpty());

    }

    @Test
    public void merge_stream() {
        Object[] testArray1 = new Object[]{
                "One", "Two"
        };
        Object[] testArray2 = new Object[]{
                "A", "B"
        };

        List<Object[]> expected = new ArrayList<>();
        expected.add( new Object[] { "One", "A" });
        expected.add( new Object[] { "One", "B" });
        expected.add( new Object[] { "Two", "A" });
        expected.add(new Object[]{"Two", "B"});

        Iterator<Object[]> iterator = ArgumentConverter.cross(testArray1, testArray2);

        Assert.assertEquals(iterator, expected.iterator());
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
