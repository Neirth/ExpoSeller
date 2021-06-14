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
package io.neirth.exposeller.client.peripherals.insertcoins;

import android.util.Log;
import io.neirth.exposeller.client.ExpoSellerApplication;
import io.neirth.exposeller.client.util.observable.DataObs;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class DummyInsertedCoinsImpl implements IInsertCoins {
    private Timer mHandler = new Timer();
    private volatile DataObs<Float> mCountedValue;

    @Inject
    public DummyInsertedCoinsImpl() {
        this.mCountedValue = new DataObs<>(0.0f);
    }

    /**
     * Observable to check the inserted value in the system
     *
     * @return The observable of checkInsertValue
     * @throws IOException Exception in case you cannot initialize the components
     */
    @Override
    public Observable checkInsertedValue() throws IOException {
        mHandler.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                // Increment value to 0,75€
                mCountedValue.postValue(mCountedValue.getValue() + 0.75f);

                // Send to logcat the information of inserted value
                Log.d(ExpoSellerApplication.LOG_TAG, "Detected inserted value in coins: " + mCountedValue.getValue());
            }
        },1000,1000);

        return mCountedValue;
    }

    /**
     * Method to return the inserted value in the system
     *
     * @param desiredValue The desired value to return
     * @return The result of operation
     * @throws IOException Exception in case you cannot initialize physical components
     */
    @Override
    public boolean returnExcessAmount(float desiredValue) throws IOException {
        return returnExcessAmount(mCountedValue.getValue(), desiredValue);
    }

    /**
     * Method to return the value desired in the system
     *
     * @param desiredValue The desired value to return
     * @param giveValue The give value
     * @return The result of operation
     * @throws IOException Exception in case you cannot initialize physical components
     */
    @Override
    public boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException {
        // Inform the circumstance via logcat
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        // Reset the timer
        mHandler.cancel();
        mHandler = new Timer();

        // Remove the observers
        mCountedValue.deleteObservers();

        // Post value to 0.00€
        mCountedValue.postValue(0.0f);

        // Indicate tha operation is valid or not using a logical comparison
        return giveValue >= desiredValue;
    }
}
