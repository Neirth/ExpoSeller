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
package io.neirth.exposeller.client.ui.adsconcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class AdsConcertActivity extends AppCompatActivity {
    // Elements of the view
    private ConstraintLayout mClAdsConcert;
    private ImageView mIvAdsConcertView;

    // Instance of View Model
    private AdsConcertViewModel mViewModel;

    /**
     * Method to inflate the view
     *
     * @param savedInstanceState The bundle with saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call to parent constructor
        super.onCreate(savedInstanceState);

        // Hide the system ui
        Utilities.hideSystemUi(getWindow());

        // Inflate the view
        setContentView(R.layout.activity_ads_concert);

        // Initialize the view
        initView();
    }

    /**
     * Private method to initialize the view
     */
    private void initView() {
        // Bind the elements
        mClAdsConcert = findViewById(R.id.clAdsConcert);
        mIvAdsConcertView = findViewById(R.id.ivAdsConcertView);

        // Set callback for click
        mIvAdsConcertView.setOnClickListener(v -> AdsConcertActivity.this.onBackPressed());

        // Initialize the view model
        mViewModel = new ViewModelProvider(this).get(AdsConcertViewModel.class);

        // Pick random elements from list and observe it
        mViewModel.pickRandomAdsList().observe(this, value -> mViewModel.setListAdvertisement(value));

        // Run the idle advertisements
        mViewModel.runIdleAdvertisment(this, mIvAdsConcertView);
    }

    @Override
    public void onBackPressed() {
        // Start a new activity
        startActivity(new Intent(AdsConcertActivity.this, MainScreenActivity.class));

        // Call to parent method
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        // Call to parent method
        super.onPause();

        // Stop Idle time
        TimeOutIdle.stopIdleHandler();
    }
}