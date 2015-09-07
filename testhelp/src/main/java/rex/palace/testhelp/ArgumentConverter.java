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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility class providing helper methods for the TestNG framework.
 */
public final class ArgumentConverter {

    /**
     * Empty constructor because this is a utility class.
     */
    private ArgumentConverter(){
    }

    /**
     * Wraps objects in one element arrays an returns an Iterator over these arrays.
     *
     * @param objects the objects to be wrapped
     * @return an iterator over single element arrays containing objects
     */
    public static Iterator<Object[]> convert(Object... objects) {
        return convert(Arrays.stream(objects));
    }

    /**
     * Wraps each element in the collection in one element
     * arrays an returns an Iterator over these arrays.
     *
     * @param collection the collection of objects to convert
     *
     * @return an iterator over single element arrays containing the objects in collection
     */
    public static Iterator<Object[]> convert(Collection<?> collection) {
        return convert(collection.stream());
    }

    /**
     * Wraps each element in the stream in one element
     * arrays an returns an Iterator over these arrays.
     *
     * @param stream the stream of objects to convert
     * @return an iterator over single element arrays containing the objects in stream
     */
    public static Iterator<Object[]> convert(Stream<?> stream) {
        return stream.map(ArgumentConverter::wrapInArray).iterator();
    }

    /**
     * Wraps an object in a one element array.
     *
     * @param object the object to be wrapped
     * @return an array containing only object
     */
    public static Object[] wrapInArray(Object object) {
        return wrapInArray0(object);
    }

    /**
     * Returns the arguments as an array.
     *
     * <p>Convenience method to ensure type safety dealing with generic arrays.
     *
     * @param objects the objects to be returned as an array
     * @return an array containing the parameters
     */
    private static Object[] wrapInArray0(Object... objects) {
        return objects;
    }

    /**
     * Returns the cross product of the sublists.
     *
     * @param lists the list whose sublists shall be cross produced
     * @return an Iterator over arrays containing the cross product
     */
    public static Iterator<Object[]> cross(List<List<?>> lists) {
        List<List<Object>> initial = new ArrayList<>();
        initial.add(new ArrayList<>());
        return cross0(initial.stream(), lists).map(List::toArray).iterator();
    }

    /**
     * Returns the cross product of the arguments.
     *
     * @param arrays the array whose subarrays shall be cross produced
     * @return an Iterator over arrays containing the cross product
     */

    public static Iterator<Object[]> cross(Object[]... arrays) {
        return cross(Arrays.stream(arrays).map(Arrays::asList).collect(Collectors.toList()));
    }

    /**
     * Calculates the cross product end recursively.
     *
     * @param stream the stream containing the cross product so far
     * @param lists the remaining lists to be cross produces with stream
     * @param <T> the type of the elements
     * @return an stream containing a list of all cross products
     */
    private static <T> Stream<List<T>> cross0(
            Stream<List<T>> stream, List<List<? extends T>> lists) {
        if (lists.isEmpty()) {
            return stream;
        }
        List<? extends T> first = lists.remove(0);
        return cross0(stream.flatMap(x -> first.stream().map(y -> wrapInList(x, y))), lists);
    }

    /**
     * Creates a new ArrayList with first list and then toAdd as its members.
     *
     * @param list the list to begin with
     * @param toAdd the element to append
     * @param <T> the type of the elements
     * @return a new list beginning with the same elements as list and afterwards has toAdd
     */
    private static <T> List<T> wrapInList(List<T> list, T toAdd) {
        List<T> toReturn = new ArrayList<>(list);
        toReturn.add(toAdd);
        return toReturn;
    }

}

/* vim:set shiftwidth=4 softtabstop=4 expandtab: */
