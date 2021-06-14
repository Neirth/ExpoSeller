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
package io.neirth.exposeller.client.ui.buytickets;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Concert;
import io.neirth.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.neirth.exposeller.client.ui.buytickets.adapter.BuyTicketConcertAdapter;
import io.neirth.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.neirth.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class BuyTicketsActivity extends AppCompatActivity {
    // Elements of view
    private ConstraintLayout mClBuyTickets;
    private TextView mTvInsertImportValue2;
    private ImageView mIvExpoSellerLogo5;
    private RecyclerView mRvBuyTickets;
    private TextView mTvNotFoundConcerts2;

    // Instance of adapter
    private BuyTicketConcertAdapter mAdapter;

    // Instance of view model
    private BuyTicketsViewModel mViewModel;

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
        setContentView(R.layout.activity_buy_tickets);

        // Initialize the view
        initView();
        initRecyclerView();

        // Prepare the timeout idle
        TimeOutIdle.initIdleHandler(() -> {
            // Start a new activity
            startActivity(new Intent(BuyTicketsActivity.this, AdsConcertActivity.class));

            // Finish the actual activity
            finish();
        });

        // Start idle timeout
        TimeOutIdle.startIdleHandler();
    }

    /**
     * Private method to init the view
     */
    private void initView() {
        // Bind elements of view
        mClBuyTickets = findViewById(R.id.clBuyTickets);
        mTvInsertImportValue2 = findViewById(R.id.tvInsertImportValue2);
        mIvExpoSellerLogo5 = findViewById(R.id.ivExpoSellerLogo5);
        mRvBuyTickets = findViewById(R.id.rvBuyTickets);
        mTvNotFoundConcerts2 = findViewById(R.id.tvNotFoundConcerts2);

        // Instance a view model
        mViewModel = new ViewModelProvider(this).get(BuyTicketsViewModel.class);
    }

    /**
     * Private method to init the recycler view
     */
    private void initRecyclerView() {
        // Instance the adapter
        mAdapter = new BuyTicketConcertAdapter();

        // Set callback to adapter
        mAdapter.setAdapterClickListener(model -> {
            if (model instanceof Concert) {
                // Cast the entity
                Concert concert = (Concert) model;

                // Start activity with extra parameters
                Intent intent = new Intent(BuyTicketsActivity.this, InsertCoinsActivity.class);
                startActivity(intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert));

                // Finish actual activity
                finish();
            }
        });

        // Set the adapter to recycler view
        mRvBuyTickets.setAdapter(mAdapter);

        // Read the concert list
        mViewModel.readConcertList().observe(this, value -> {
            // Detect if the concert list is empty or not
            if (value.size() >= 1) {
                mTvNotFoundConcerts2.setVisibility(View.GONE);
            } else {
                mTvNotFoundConcerts2.setVisibility(View.VISIBLE);
            }

            // Set the concert list
            mAdapter.setConcertList(value);
        });
    }

    /**
     * Method to detect if back pressed
     */
    @Override
    public void onBackPressed() {
        // Start a new activity
        startActivity(new Intent(BuyTicketsActivity.this, MainScreenActivity.class));

        // Call to parent method
        super.onBackPressed();
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