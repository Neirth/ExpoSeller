/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.ui.insertcoins;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Concert;
import io.neirth.exposeller.client.domain.Ticket;
import io.neirth.exposeller.client.service.UserService;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.observable.DataObs;

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
