package io.smartinez.exposeller.client.ui.buytickets;

import androidx.lifecycle.ViewModel;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class BuyTicketsViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public BuyTicketsViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }


}
