package io.smartinez.exposeller.client.peripherals.insertcoins;

import androidx.lifecycle.LiveData;

import java.io.IOException;

public interface IInsertCoins {
    LiveData<Float> checkInsertedValue() throws IOException;
    boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException;
}
