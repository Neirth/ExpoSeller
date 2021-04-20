package io.smartinez.exposeller.client.ui.insertcoins;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.service.UserService;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    private UserService mUsersService;
    private ExecutorService mExecutorService;

    @Inject
    public InsertCoinsViewModel(UserService usersService, ExecutorService executorService) {
        this.mUsersService = usersService;
        this.mExecutorService = executorService;
    }

    public Future<Void> buyTicket(LifecycleOwner lifecycleOwner, Concert concert, TextView tvConcertValue, TextView tvInsertedValue, TextView tvReturnValue) {
        CompletableFuture<Void> futureResult = new CompletableFuture<>();

        mExecutorService.execute(() -> {
            tvConcertValue.setText(String.valueOf(concert.getCost()));

            try {
                mUsersService.checkInsertCoins().observe(lifecycleOwner, value -> {
                    try {
                        if (concert.getCost().equals(value)) {
                            int friendlyId = mUsersService.generateFreeTicketFriendlyId();
                            Ticket ticket = new Ticket(null, concert.getDocId(), friendlyId, false, concert.getEventDate());

                            mUsersService.buyTicket(ticket, () -> {
                                tvReturnValue.setText(String.valueOf(value - concert.getCost()));
                                mUsersService.returnValueCoins(concert.getCost(), value);

                                futureResult.complete(null);
                            });
                        } else {
                            tvInsertedValue.setText(String.valueOf(value));
                        }
                    } catch (IOException e) {
                        futureResult.completeExceptionally(e);
                    }
                });
            } catch (IOException e) {
                futureResult.completeExceptionally(e);
            }
        });

        return futureResult;
    }

    public void cancelBuyTicket(Concert concert) throws IOException {
        Float insertedCoins = mUsersService.checkInsertCoins().getValue();
        mUsersService.returnValueCoins(concert.getCost(), insertedCoins != null ? insertedCoins : 0);
    }
}
