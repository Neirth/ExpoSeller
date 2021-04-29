package io.smartinez.exposeller.client.peripherals.insertcoins;

import android.util.Log;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.util.observable.DataObs;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class DummyInsertedCoinsImpl implements IInsertCoins {
    private Timer mHandler = new Timer();
    private final DataObs<Float> mCountedValue;

    @Inject
    public DummyInsertedCoinsImpl() {
        this.mCountedValue = new DataObs<>(0.0f);
    }

    @Override
    public Observable checkInsertedValue() throws IOException {
        mHandler.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                mCountedValue.postValue(mCountedValue.getValue() + 0.75f);
                Log.d(ExpoSellerApplication.LOG_TAG, "Detected inserted value in coins: " + mCountedValue.getValue());
            }
        },0,1000);

        return mCountedValue;
    }

    @Override
    public boolean returnExcessAmount(float giveValue) throws IOException {
        return returnExcessAmount(mCountedValue.getValue(), giveValue);
    }

    @Override
    public boolean returnExcessAmount(float desiredValue, float giveValue) throws IOException {
        Log.d(ExpoSellerApplication.LOG_TAG, "I'm a dummy method, checking if giveValue is greater or equal than desired value");

        mHandler.cancel();
        mHandler = new Timer();
        mCountedValue.deleteObservers();
        mCountedValue.postValue(0.0f);

        return giveValue >= desiredValue;
    }
}
