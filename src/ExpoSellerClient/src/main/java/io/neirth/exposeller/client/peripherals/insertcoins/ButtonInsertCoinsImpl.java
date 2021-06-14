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

import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import io.neirth.exposeller.client.ExpoSellerApplication;
import io.neirth.exposeller.client.util.observable.DataObs;

/**
 * Another example implementation using the Raspberry Pi 3 GPIO ports.
 *
 * This class has been created with the intention of providing an example
 * of how to transform a real electrical signal into a meter that we can
 * extrapolate to the rest of the application.
 */
@Singleton
public class ButtonInsertCoinsImpl implements IInsertCoins {
    private volatile DataObs<Float> mCountedValue;
    private final PeripheralManager mPeripheralManager;

    private Thread mButtonThread = null;

    @Inject
    public ButtonInsertCoinsImpl() {
        this.mCountedValue = new DataObs<>(0.0f);
        this.mPeripheralManager = PeripheralManager.getInstance();
    }

    /**
     * Observable to check the inserted value in the system
     *
     * @return The observable of checkInsertValue
     * @throws IOException Exception in case you cannot initialize the components
     */
    public Observable checkInsertedValue() throws IOException {
        if (mButtonThread == null) {
            startPhysicalCounter();
        }

        return mCountedValue;
    }

    /**
     * Method to return the inserted value in the system
     *
     * @param desiredValue The desired value to return
     * @return The result of operation
     * @throws IOException Exception in case you cannot initialize physical components
     */
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
    public boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException {
        // Inform the circumstance via logcat
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        // Because, we don't have any device to return the physical value, we reset the counter
        mCountedValue.postValue(0.0f);

        // Stop the physical counter
        stopPhysicalCounter();

        // Indicate tha operation is valid or not using a logical comparison
        return giveValue >= desiredValue;
    }

    /**
     * Private method to start a physical counter thread
     *
     * This method opens in a new thread the GPIO pin in Raspberry Pi 3 and detects
     * the High Voltage from electric circuit, transforming this value into a Java Boolean
     *
     * The GPIO pin used is BCM4 and his documentation is published in Android Things page
     *
     * @see <a href="https://developer.android.com/things/hardware/raspberrypi">Raspberry Pi 3 (Android Things)</a>
     */
    private void startPhysicalCounter() {
        // Prepare button thread
        mButtonThread = new Thread(()  -> {
            try {
                // Prepare thread for looper
                Looper.prepare();

                // Open GPIO in Raspberry Pi 3
                Gpio gpio = mPeripheralManager.openGpio("BCM4");

                // Configure GPIO pin
                gpio.setDirection(Gpio.DIRECTION_IN);
                gpio.setActiveType(Gpio.ACTIVE_HIGH);
                gpio.setEdgeTriggerType(Gpio.EDGE_RISING);

                // Register callback
                gpio.registerGpioCallback((value) -> {
                    try {
                        if (value.getValue()) {
                            // Post value to DataObs
                            mCountedValue.postValue(mCountedValue.getValue() + 0.75f);

                            // Send to logcat the information of inserted value
                            Log.d(ExpoSellerApplication.LOG_TAG, "Detected inserted value in coins: " + mCountedValue.getValue());

                            // Limit the number of pulsations to 1 per second
                            Thread.sleep(1000);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    return true;
                });

                // Run a looper
                Looper.loop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Start button thread
        mButtonThread.start();
    }

    /**
     * Private method to stop a physical counter thread
     */
    private void stopPhysicalCounter() {
        if (mButtonThread != null) {
            mButtonThread.interrupt();
            mButtonThread = null;
        }
    }
}
