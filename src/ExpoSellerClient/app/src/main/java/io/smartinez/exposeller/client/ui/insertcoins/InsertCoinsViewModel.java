package io.smartinez.exposeller.client.ui.insertcoins;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    private UserService mUsersService;

    @Inject
    public InsertCoinsViewModel(UserService mUsersService) {
        this.mUsersService = mUsersService;
    }

    public void buyTicket(LifecycleOwner lifecycleOwner, Concert concert, TextView tvConcertValue, TextView tvInsertedValue, TextView tvReturnValue) throws IOException {
        tvConcertValue.setText(String.valueOf(concert.getCost()));

        mUsersService.checkInsertCoins().observe(lifecycleOwner, value -> {
            if (concert.getCost().equals(value)) {
                mUsersService.generateFreeTicketFriendlyId().observe(lifecycleOwner, result -> {
                    Ticket ticket = new Ticket(null, concert.getDocId(), result, false, concert.getEventDate());

                    try {
                        mUsersService.buyTicket(ticket, () -> {
                            tvReturnValue.setText(String.valueOf(value - concert.getCost()));
                            mUsersService.returnValueCoins(concert.getCost(), value);
                        });
                    } catch (IOException e) {
                        mUsersService.returnValueCoins(0.0f, value);
                    }
                });
            } else {
                tvInsertedValue.setText(String.valueOf(value));
            }
        });
    }

    public void cancelBuyTicket(Concert concert) throws IOException {
        Float insertedCoins = mUsersService.checkInsertCoins().getValue();
        mUsersService.returnValueCoins(concert.getCost(), insertedCoins != null ? insertedCoins : 0);
    }
}
