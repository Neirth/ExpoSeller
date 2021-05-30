package io.smartinez.exposeller.client.ui.insertcoins;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
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
import io.smartinez.exposeller.client.util.TimeOutIdle;
import io.smartinez.exposeller.client.util.observable.DataObs;

@HiltViewModel
public class InsertCoinsViewModel extends ViewModel {
    // Instance for use case user
    private final UserService mUsersService;

    // Instance for executor service
    private final ExecutorService mExecutorService;

    @Inject
    public InsertCoinsViewModel(UserService usersService, ExecutorService executorService) {
        this.mUsersService = usersService;
        this.mExecutorService = executorService;
    }

    /**
     * Method for execute the buy operation
     *
     * @param activity Activity instance
     * @param concert Concert instance
     * @param tvInsertedValue Instance of TextView Inserted Value
     * @param tvReturnValue Instance of TextView Return Value
     * @param btnCancelOperation Instance of Button Cancel Operation
     * @return Return a future object
     */
    public CompletableFuture<String> buyTicket(Activity activity, Concert concert, TextView tvInsertedValue, TextView tvReturnValue, Button btnCancelOperation) {
        // Prepare a future operation instance
        CompletableFuture<String> futureResult = new CompletableFuture<>();

        // Run in background the operation
        mExecutorService.execute(() -> {
            try {
                // Observe the value inserted in the system
                mUsersService.checkInsertCoins().addObserver((value, args) -> {
                    try {
                        // Cast the value from parameters
                        float valueInsertedCoins = (Float) args;

                        // Reset the time idle
                        TimeOutIdle.resetIdleHandle();

                        // Change the value of inserted coins
                        activity.runOnUiThread(() -> tvInsertedValue.setText(String.format(Locale.getDefault(),activity.getString(R.string.inserted_value), valueInsertedCoins)));

                        // Check if inserted value is higher than concert cost
                        if (valueInsertedCoins >= concert.getCost()) {
                            // Block the cancel button because is a critical operation
                            activity.runOnUiThread(() -> btnCancelOperation.setClickable(false));

                            // Get the friendly id for ticket
                            int friendlyId = mUsersService.generateTicketFriendlyId();

                            // Create the instance of ticket
                            Ticket ticket = new Ticket(null, concert.getDocId(), friendlyId, false, concert.getEventDate());

                            // Get the value to return
                            activity.runOnUiThread(() -> tvReturnValue.setText(String.format(Locale.getDefault(), activity.getString(R.string.return_value), valueInsertedCoins - concert.getCost())));

                            // Run the buy operation of ticket
                            mUsersService.buyTicket(ticket, uriTicket -> {
                                // Remove callbacks to value
                                if (value instanceof DataObs)
                                    value.deleteObservers();

                                // Return the excess value of the coins
                                mUsersService.returnValueCoins(concert.getCost());

                                // Inform the operation is complete
                                futureResult.complete(uriTicket);
                            });
                        }
                    } catch (IOException e) {
                        // Inform the operation is complete exceptionally
                        futureResult.completeExceptionally(e);
                    }
                });
            } catch (IOException e) {
                // Inform the operation is complete exceptionally
                futureResult.completeExceptionally(e);
            }
        });

        // Return the future operation
        return futureResult;
    }

    /**
     * Method for return the value of the operation
     *
     * @param concert The concert object
     * @throws IOException Exception in case you cannot initialize physical components.
     */
    public void cancelBuyTicket(Concert concert) throws IOException {
        mUsersService.returnValueCoins(concert != null ? concert.getCost() : 0);
    }
}
