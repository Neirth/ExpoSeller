package io.smartinez.exposeller.client.ui.insertcoins;

import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    private UserService mUsersService;
    private ExecutorService mExecutorService;

    @Inject
    public InsertCoinsViewModel(UserService usersService, ExecutorService executorService) {
        this.mUsersService = usersService;
        this.mExecutorService = executorService;
    }

    public CompletableFuture<String> buyTicket(LifecycleOwner lifecycleOwner, Concert concert, TextView tvInsertedValue, TextView tvReturnValue) {
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        mExecutorService.execute(() -> {
            try {
                mUsersService.checkInsertCoins().observe(lifecycleOwner, value -> {
                    try {
                        if (value >= concert.getCost()) {
                            int friendlyId = mUsersService.generateTicketFriendlyId();

                            Ticket ticket = new Ticket(null, concert.getDocId(), friendlyId, false, concert.getEventDate());

                            mUsersService.buyTicket(ticket, uriTicket -> {
                                tvReturnValue.setText(String.valueOf(value - concert.getCost()));
                                mUsersService.returnValueCoins(concert.getCost(), value);

                                futureResult.complete(uriTicket);
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
