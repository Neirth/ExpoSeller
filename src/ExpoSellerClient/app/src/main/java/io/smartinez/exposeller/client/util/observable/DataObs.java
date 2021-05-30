package io.smartinez.exposeller.client.util.observable;

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
