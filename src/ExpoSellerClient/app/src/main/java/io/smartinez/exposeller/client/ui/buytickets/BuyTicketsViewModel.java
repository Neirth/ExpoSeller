package io.smartinez.exposeller.client.ui.buytickets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class BuyTicketsViewModel extends ViewModel {
    // Instance for use case user
    private final UserService mUsersService;

    // Instance for executor service
    private final ExecutorService mExecutorService;

    @Inject
    public BuyTicketsViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to get a live data with concert list
     *
     * @return The live data list
     */
    public LiveData<List<Concert>> readConcertList() {
        // Instance a mutable live data
        MutableLiveData<List<Concert>> concertList = new MutableLiveData<>();

        // Run background the query
        mExecutorService.execute(() -> {
            try {
                concertList.postValue(mUsersService.readConcertList());
            } catch (IOException e) {
                concertList.postValue(Collections.emptyList());
            }
        });

        // Return the live data
        return concertList;
    }
}
