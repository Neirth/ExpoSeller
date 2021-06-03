/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.smartinez.exposeller.client.ui.insertcoins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.smartinez.exposeller.client.ui.pickupticket.PickupTicketActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;
import io.smartinez.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class InsertCoinsActivity extends AppCompatActivity {
    // Constant with extra id
    public static final String EXTRA_CONCERT = "io.smartinez.exposeller.client.ui.insertcoins.concertData";

    // Elements of view
    private ConstraintLayout mClInsertCoins;
    private ImageView mIvInsertCoins;
    private ImageView mIvExpoSellerLogo3;
    private TextView mTvInsertImportValue;
    private TextView mTvConcertChoice;
    private TextView mTvConcertValue;
    private TextView mTvInsertedValue;
    private TextView mTvReturnValue;
    private TextView mTvInsertCoins;
    private Button mBtnCancelOperation2;

    // Instance of view model
    private InsertCoinsViewModel mViewModel;

    // Instance of object
    private Concert mConcert;

    /**
     * Method to inflate the view
     *
     * @param savedInstanceState The bundle with saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent method
        super.onCreate(savedInstanceState);

        // Hide the system ui
        Utilities.hideSystemUi(getWindow());

        // Inflate the view
        setContentView(R.layout.activity_insert_coins);

        // Get the concert object
        mConcert = getIntent().getParcelableExtra(EXTRA_CONCERT);

        // Initialize the view
        initView();

        // Prepare the timeout idle
        TimeOutIdle.initIdleHandler(() -> {
            try {
                // Cancel the buy ticket operation
                mViewModel.cancelBuyTicket(null);

                // Start a new activity
                startActivity(new Intent(InsertCoinsActivity.this, AdsConcertActivity.class));

                // Finish the actual activity
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Start idle timeout
        TimeOutIdle.startIdleHandler();
    }

    /**
     * Private method to init the view
     */
    private void initView() {
        // Bind elements of view
        mClInsertCoins = findViewById(R.id.clInsertCoins);
        mIvInsertCoins = findViewById(R.id.ivInsertCoins);
        mTvInsertImportValue = findViewById(R.id.tvInsertImportValue);
        mTvConcertChoice = findViewById(R.id.tvConcertChoice);
        mTvConcertValue = findViewById(R.id.tvConcertValue);
        mTvInsertedValue = findViewById(R.id.tvInsertedValue);
        mTvReturnValue = findViewById(R.id.tvReturnValue);
        mTvInsertCoins = findViewById(R.id.tvInsertCoins);
        mBtnCancelOperation2 = findViewById(R.id.btnCancelOperation2);

        // Bind the cancellation callback
        mBtnCancelOperation2.setOnClickListener(v -> InsertCoinsActivity.this.onBackPressed());

        // Bind the data
        mTvConcertValue.setText(String.format(Locale.getDefault(), getString(R.string.concert_value), mConcert.getCost()));
        mTvConcertChoice.setText(String.format(Locale.getDefault(), getString(R.string.concert_choice), mConcert.getName()));
        mTvInsertedValue.setText(String.format(Locale.getDefault(), getString(R.string.inserted_value), 0.0));
        mTvReturnValue.setText(String.format(Locale.getDefault(), getString(R.string.return_value), 0.0));

        // Instance a view model
        mViewModel = new ViewModelProvider(this).get(InsertCoinsViewModel.class);

        // Bind the callback of buy operation
        mViewModel.buyTicket(this, mConcert, mTvInsertedValue, mTvReturnValue, mBtnCancelOperation2)
            .thenApply(uriTicket -> {
                runOnUiThread(() -> {
                    // Start the button with extra data
                    Intent intent = new Intent(InsertCoinsActivity.this, PickupTicketActivity.class);
                    startActivity(intent.putExtra(PickupTicketActivity.EXTRA_TICKET, uriTicket));

                    // Finish the actual activity
                    finish();
                });

                // Return a null value for finish the future operation
                return null;
            }).exceptionally(ex -> {
                runOnUiThread(() -> {
                    try {
                        // Show the toast with error information
                        Utilities.showToast(InsertCoinsActivity.this, R.string.buy_error);

                        // Cancel the operation
                        mViewModel.cancelBuyTicket(null);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });

                // Return a null value for finish the future operation
                return null;
            });
    }

    /**
     * Method to detect if back pressed
     */
    @Override
    public void onBackPressed() {
        try {
            // Cancel the operation
            mViewModel.cancelBuyTicket(null);
        } catch (IOException e) {
            // Print the stack trace
            e.printStackTrace();
        } finally {
            // Start a new activity
            startActivity(new Intent(InsertCoinsActivity.this, MainScreenActivity.class));

            // Call to parent method
            super.onBackPressed();
        }
    }

    /**
     * Method to detect if the activity is pause
     */
    @Override
    protected void onPause() {
        // Call to parent method
        super.onPause();

        // Stop the idle time
        TimeOutIdle.stopIdleHandler();
    }

    /**
     * Method to detect the user interactions
     */
    @Override
    public void onUserInteraction() {
        // Call to parent method
        super.onUserInteraction();

        // Reset the idle time
        TimeOutIdle.resetIdleHandle();
    }
}