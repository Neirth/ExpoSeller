package io.smartinez.exposeller.client.ui.adsconcert;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class AdsConcertViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public AdsConcertViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }
}
