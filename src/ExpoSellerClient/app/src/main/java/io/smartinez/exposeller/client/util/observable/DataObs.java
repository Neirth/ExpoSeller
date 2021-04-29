package io.smartinez.exposeller.client.util.observable;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

public class DataObs<T> extends Observable {
    private final AtomicReference<T> mCountedValue = new AtomicReference<>();

    public DataObs() {
    }

    public DataObs(T v) {
        mCountedValue.set(v);
    }

    public synchronized void postValue(T value) {
        mCountedValue.set(value);
        setChanged();
        notifyObservers(value);
    }

    public synchronized T getValue() {
        return mCountedValue.get();
    }

}
