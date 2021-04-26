package io.smartinez.exposeller.client.peripherals.insertcoins;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.ExpoSellerApplication;

@ViewModelScoped
public class ButtonInsertCoinsImpl implements IInsertCoins {
//    private PeripheralManager mPeripheralManager = PeripheralManager.getInstance();
    private PeripheralManager mPeripheralManager = null;

    @Inject
    public ButtonInsertCoinsImpl() {
    }

    public LiveData<Float> checkInsertedValue() throws IOException {
        Gpio gpio = mPeripheralManager.openGpio("");

        MutableLiveData<Float> countedValue = new MutableLiveData<>(0f);

        gpio.registerGpioCallback((gpioAux) -> {
            try {
                if (gpioAux.getValue()) {
                    countedValue.postValue(countedValue.getValue() + 0.50f);
                }

                return gpioAux.getValue();
            } catch (IOException e) {
                return false;
            }
        });

        return countedValue;
    }

    public boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException {
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        return giveValue >= desiredValue;
    }
}
