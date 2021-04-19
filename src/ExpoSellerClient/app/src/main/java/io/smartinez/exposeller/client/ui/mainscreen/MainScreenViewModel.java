package io.smartinez.exposeller.client.ui.mainscreen;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class MainScreenViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public MainScreenViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }
}
