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

package de.leifaktor.robbie.controllers.clock;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.leifaktor.robbie.api.controllers.clock.Clock;
import de.leifaktor.robbie.api.controllers.clock.ClockRestorer;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public interface ClockCallBack<T> extends FutureCallback<T> {

    @Override
    default void onSuccess(T result) {
        //can't happen
    }

    @Override
    public void onFailure(Throwable t);

    static <T> void addCallback(
            ListenableFuture<T> future,
            ClockCallBack<T> callBack,
            ExecutorService executor) {
        Futures.addCallback(future, callBack, executor);
    }

}
