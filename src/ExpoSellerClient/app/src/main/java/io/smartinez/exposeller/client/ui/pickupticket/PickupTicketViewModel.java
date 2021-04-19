package io.smartinez.exposeller.client.ui.pickupticket;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import io.smartinez.exposeller.client.service.UserService;

public class PickupTicketViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public PickupTicketViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }
}
