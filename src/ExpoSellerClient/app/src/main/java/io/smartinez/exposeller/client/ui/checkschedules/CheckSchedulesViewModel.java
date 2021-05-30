package io.smartinez.exposeller.client.ui.checkschedules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class CheckSchedulesViewModel extends ViewModel {
    // Instance for use case user
    private final UserService mUsersService;

    // Instance for executor service
    private final ExecutorService mExecutorService;

    @Inject
    public CheckSchedulesViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to search in background the concerts with specific date
     *
     * @param date The date object
     * @return The live data list
     */
    public LiveData<List<Concert>> searchConcertWithDay(Date date) {
        // Instance a mutable live data
        MutableLiveData<List<Concert>> concertList = new MutableLiveData<>();

        // Run background the query
        mExecutorService.execute(() -> {
            try {
                concertList.postValue(mUsersService.searchConcertWithDay(date));
            } catch (IOException e) {
                concertList.postValue(Collections.emptyList());
            }
        });

        // Return the live data
        return concertList;
    }
}
