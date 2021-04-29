package io.smartinez.exposeller.client.ui.insertcoins;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.Ticket;
import io.smartinez.exposeller.client.service.UserService;
import io.smartinez.exposeller.client.util.observable.DataObs;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    private final UserService mUsersService;
    private final ExecutorService mExecutorService;

    @Inject
    public InsertCoinsViewModel(UserService usersService, ExecutorService executorService) {
        this.mUsersService = usersService;
        this.mExecutorService = executorService;
    }

    public CompletableFuture<String> buyTicket(Activity activity, Concert concert, TextView tvInsertedValue, TextView tvReturnValue) {
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        mExecutorService.execute(() -> {
            try {
                mUsersService.checkInsertCoins().addObserver((value, args) -> {
                    try {
                        float valueInsertedCoins = (Float) args;

                        activity.runOnUiThread(() -> tvInsertedValue.setText(String.format(Locale.getDefault(),activity.getString(R.string.inserted_value), valueInsertedCoins)));

                        if (valueInsertedCoins >= concert.getCost()) {
                            int friendlyId = mUsersService.generateTicketFriendlyId();

                            Ticket ticket = new Ticket(null, concert.getDocId(), friendlyId, false, concert.getEventDate());
                            activity.runOnUiThread(() -> tvReturnValue.setText(String.format(Locale.getDefault(), activity.getString(R.string.return_value), valueInsertedCoins - concert.getCost())));

                            mUsersService.buyTicket(ticket, uriTicket -> {
                                if (value instanceof DataObs)
                                    value.deleteObservers();

                                Log.d(ExpoSellerApplication.LOG_TAG, "Getting a ticket!");

                                mUsersService.returnValueCoins(concert.getCost());
                                futureResult.complete(uriTicket);
                            });
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
        mUsersService.returnValueCoins(concert != null ? concert.getCost() : 0);
    }
}
