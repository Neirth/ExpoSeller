package io.smartinez.exposeller.client.peripherals.insertcoins;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Provides;
import dagger.hilt.android.scopes.ActivityScoped;
import io.smartinez.exposeller.client.ExpoSellerApplication;

@ActivityScoped
public class ButtonInsertCoinsImpl implements IInsertCoins {
    private PeripheralManager mPeripheralManager = PeripheralManager.getInstance();

    @Inject
    public ButtonInsertCoinsImpl() {
    }

    public LiveData<Float> checkInsertedValue() throws IOException {
        Gpio gpio = mPeripheralManager.openGpio("");

        MutableLiveData<Float> countedValue = new MutableLiveData<>(0f);

        gpio.registerGpioCallback((gpioAux) -> {
            try {
                if (gpioAux.getValue()) {
                    countedValue.setValue(countedValue.getValue() + 0.50f);
                }

                return gpioAux.getValue();
            } catch (IOException e) {
                return false;
            }
        });

        return countedValue;
    }

    public boolean returnExcessAmount(float desiredValue, float giveValue) {
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        return giveValue >= desiredValue;
    }
}
