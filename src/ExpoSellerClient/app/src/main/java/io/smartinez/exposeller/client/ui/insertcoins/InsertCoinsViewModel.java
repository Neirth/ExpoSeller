package io.smartinez.exposeller.client.ui.insertcoins;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public InsertCoinsViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }
}
