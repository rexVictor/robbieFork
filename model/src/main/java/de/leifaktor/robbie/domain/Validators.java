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

import java.util.List;

/**
 * Utility class to validate model data.
 */
final class Validators {


    /**
     * Private constructor, since there are only static methods.
     */
    private Validators() { }

    /**
     * Returns the parameter, if it is valid.
     *
     * <p> It is considered vaild, if it is not null, non of its sublists are null
     * and none of the entries of the sublist are null.
     *
     * @param lists the list of lists of objects to check.
     * @param <V> the type the sublists contain
     * @return the parameter
     * @throws NullPointerException if lists is null or any of it sublists are null or
     *         or any of the entries of the sublists are null
     * @throws IndexOutOfBoundsException if any list is empty or if the entry lists,
     *         do not have same length
     */
    public static <V> List<List<V>> requireNonNullAndSameSize(List<List<V>> lists) {
        int firstSize = lists.get(0).size();
        if (firstSize <= 0) {
            throw new IndexOutOfBoundsException();
        }
        if (lists.stream().anyMatch(subList -> subList == null
                    || subList.stream().anyMatch( entry -> entry == null ))) {
            throw new NullPointerException();
        }
        if (lists.stream().anyMatch(subList -> subList.size() != firstSize)) {
            throw new IndexOutOfBoundsException();
        }
        return lists;
    }
}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
