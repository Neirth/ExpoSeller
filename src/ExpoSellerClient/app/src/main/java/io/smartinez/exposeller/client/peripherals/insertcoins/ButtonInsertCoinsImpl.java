package io.smartinez.exposeller.client.peripherals.insertcoins;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.util.observable.DataObs;

@Singleton
public class ButtonInsertCoinsImpl implements IInsertCoins {
    private final DataObs<Float> mCountedValue;
    private final PeripheralManager mPeripheralManager;

    @Inject
    public ButtonInsertCoinsImpl() {
        this.mCountedValue = new DataObs<>(0.0f);
        this.mPeripheralManager = PeripheralManager.getInstance();
    }

    public Observable checkInsertedValue() throws IOException {
        Gpio gpio = mPeripheralManager.openGpio("");

        Observable observable = new Observable();

        gpio.registerGpioCallback((gpioAux) -> {
            try {
                if (gpioAux.getValue()) {
                    mCountedValue.postValue(mCountedValue.getValue() + 0.50f);
                    observable.notifyObservers(mCountedValue.getValue());
                }

                return gpioAux.getValue();
            } catch (IOException e) {
                return false;
            }
        });

        return observable;
    }

    public boolean returnExcessAmount(float giveValue) throws IOException {
        return returnExcessAmount(mCountedValue.getValue(), giveValue);
    }

    public boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException {
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        mCountedValue.postValue(0.0f);

        return giveValue >= desiredValue;
    }
}
