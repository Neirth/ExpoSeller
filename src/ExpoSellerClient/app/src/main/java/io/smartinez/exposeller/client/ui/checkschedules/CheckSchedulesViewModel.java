package io.smartinez.exposeller.client.ui.checkschedules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class CheckSchedulesViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public CheckSchedulesViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }

    public LiveData<List<Concert>> searchConcertWithDay(Date date) {
        return mUsersService.searchConcertWithDay(date);
    }
}
