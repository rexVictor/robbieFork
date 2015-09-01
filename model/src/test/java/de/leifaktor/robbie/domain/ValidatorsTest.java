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

package de.leifaktor.robbie.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the Validators utility class.
 */
public class ValidatorsTest {

    /**
     * Constructor for the test.
     */
    public ValidatorsTest() {}

    @Test(expectedExceptions = NullPointerException.class)
    public void requireNonNullAndSameSize_null() {
        Validators.requireNonNullAndSameSize(null);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void requireNonNullAndSameSize_emptyList() {
        List<List<Integer>> emptyList = new ArrayList<>();
        Validators.requireNonNullAndSameSize(emptyList);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void requireNonNullAndSameSize_emptySubList() {
        List<List<Integer>> list = new ArrayList<>();
        list.add( new ArrayList<>());
        Validators.requireNonNullAndSameSize(list);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void requireNonNullAndSameSize_subListContainsNull() {
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> subList = new ArrayList<>();
        subList.add(null);
        list.add(subList);
        Validators.requireNonNullAndSameSize(list);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void requireNonNullAndSameSize_subListsNotSameLength() {
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> subList1 = Arrays.asList(0);
        List<Integer> subList2 = Arrays.asList(0,1);
        list.add(subList1);
        list.add(subList2);
        Validators.requireNonNullAndSameSize(list);
    }

    @Test
    public void requireNonNullAndSameSize_TwoTimesTwoMatrix() {
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> subList1 = Arrays.asList(0,1);
        List<Integer> subList2 = Arrays.asList(2,3);
        list.add(subList1);
        list.add(subList2);
        List<List<Integer>> valid = Validators.requireNonNullAndSameSize(list);
        Assert.assertSame(valid, list);
    }

    @Test
    public void testValidatorsIsUtilityClass() throws Exception {
        TestHelper.assertUtilityClassWellDefined(Validators.class);
    }


}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
