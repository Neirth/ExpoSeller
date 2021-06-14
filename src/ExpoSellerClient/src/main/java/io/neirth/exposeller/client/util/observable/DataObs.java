/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.util.observable;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class to allow observe values in background threads
 *
 * @param <T> The type of object
 */
public class DataObs<T> extends Observable {
    // Atomic reference to value
    private final AtomicReference<T> mCountedValue = new AtomicReference<>();

    public DataObs(T v) {
        mCountedValue.set(v);
    }

    /**
     * Method to post the value into the object
     *
     * @param value The value
     */
    public synchronized void postValue(T value) {
        // Set the value into atomic reference
        mCountedValue.set(value);

        // Set the dataset has changed
        setChanged();

        // Notify the observers
        notifyObservers(value);
    }

    /**
     * Method to get the value from the object
     *
     * @return The value
     */
    public synchronized T getValue() {
        return mCountedValue.get();
    }

}
