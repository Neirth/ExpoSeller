package io.smartinez.exposeller.client.ui.adsconcert;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class AdsConcertViewModel extends ViewModel {
    private UserService mUsersService;
    private ExecutorService mExecutorService;

    @Inject
    public AdsConcertViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
    }

    public LiveData<List<AdBanner>> pickRandomAdsList() {
        MutableLiveData<List<AdBanner>> mutableAd = new MutableLiveData<>();

        mExecutorService.execute(() -> {
            try {
                mutableAd.postValue(mUsersService.pickRandomAdsList());
            } catch (IOException e) {
                mutableAd.postValue(Collections.emptyList());
            }
        });

        return mutableAd;
    }
}
