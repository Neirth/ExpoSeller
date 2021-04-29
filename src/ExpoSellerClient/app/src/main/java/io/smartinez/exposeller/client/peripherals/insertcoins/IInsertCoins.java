package io.smartinez.exposeller.client.peripherals.insertcoins;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.Observable;

public interface IInsertCoins {
    Observable checkInsertedValue() throws IOException;
    boolean returnExcessAmount(float desiredValue) throws IOException;
    boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException;

}
