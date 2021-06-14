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
package io.neirth.exposeller.client.ui.pickupticket;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.peripherals.ticketgenerator.TicketType;
import io.neirth.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.neirth.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class PickupTicketActivity extends AppCompatActivity {
    // Constant with extra id
    public static final String EXTRA_TICKET = "io.smartinez.exposeller.client.ui.pickupticket.ticketData";

    // Elements of view
    private ConstraintLayout mClPickupTicket;
    private ImageView mIvPickupTicket;
    private TextView mTvPickupTicket;
    private ImageView mIvExpoSellerLogo4;
    private ImageView mIvQrCode;
    private TextView mTvPickupText1;
    private TextView mTvPickupText2;
    private TextView mTvPrintingTicket;

    // Instance of view model
    private PickupTicketViewModel mViewModel;

    // Instance of uri
    private String mUriTicket;

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
        setContentView(R.layout.activity_pickup_ticket);

        // Get the uri ticket
        mUriTicket = getIntent().getStringExtra(EXTRA_TICKET);

        // Initialize the view
        initView();

        // Prepare the timeout idle
        TimeOutIdle.initIdleHandler(() -> {
            // Start a new activity
            startActivity(new Intent(PickupTicketActivity.this, AdsConcertActivity.class));

            // Finish the actual activity
            finish();
        });

        // Start idle timeout
        TimeOutIdle.startIdleHandler(30 * 1000);
    }

    /**
     * Private method to init the view
     */
    private void initView() {
        // Bind elements of view
        mClPickupTicket = findViewById(R.id.clPickupTicket);
        mIvPickupTicket = findViewById(R.id.ivPickupTicket);
        mTvPickupTicket = findViewById(R.id.tvPickupTicket);
        mIvExpoSellerLogo4 = findViewById(R.id.ivExpoSellerLogo4);
        mIvQrCode = findViewById(R.id.ivQrCode);
        mTvPickupText1 = findViewById(R.id.tvPickupText1);
        mTvPickupText2 = findViewById(R.id.tvPickupText2);
        mTvPrintingTicket = findViewById(R.id.tvPrintingTicket);

        // Instance a view model
        mViewModel = new ViewModelProvider(this).get(PickupTicketViewModel.class);

        // Detect the type of instance of ITicketGenerator to show QR ImageView or not
        if (mViewModel.getTicketImplType() == TicketType.VIRTUAL || mViewModel.getTicketImplType() == TicketType.HYBRID) {
            mViewModel.generateTicketQrCode(this, mIvQrCode, mUriTicket);
        } else if (mViewModel.getTicketImplType() == TicketType.PHYSICAL){
            mIvQrCode.setVisibility(View.GONE);
            mTvPrintingTicket.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method to detect if back pressed
     */
    @Override
    public void onBackPressed() {
        // Start a new activity
        startActivity(new Intent(PickupTicketActivity.this, MainScreenActivity.class));

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
        TimeOutIdle.resetIdleHandle(30 * 1000);
    }
}