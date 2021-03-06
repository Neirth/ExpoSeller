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
package io.neirth.exposeller.client.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.ExpoSellerApplication;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.neirth.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.neirth.exposeller.client.ui.checkschedules.CheckSchedulesActivity;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class MainScreenActivity extends AppCompatActivity {
    // Elements of view
    private ConstraintLayout mClMainScreen;
    private Button mBtnCheckSchedules;
    private Button mBtnBuyTickets;
    private ImageView mIvExpoSellerLogo1;
    private Guideline mGlHorizontalSeparator1;
    private Guideline mGlHorizontalSeparator2;
    private View mFgAdminLogin;
    private ConstraintLayout mClMainUserScreen;
    private ImageView mIvAdminLogin;

    /**
     * Method to inflate the view
     *
     * @param savedInstanceState The bundle with saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call to parent method
        super.onCreate(savedInstanceState);

        // Hide the system ui
        Utilities.hideSystemUi(getWindow());

        // Inflate the view
        setContentView(R.layout.activity_main_screen);

        // Initialize the view
        initView();

        // Prepare the timeout idle
        TimeOutIdle.initIdleHandler(() -> {
            // Start a new activity
            startActivity(new Intent(MainScreenActivity.this, AdsConcertActivity.class));

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
        mClMainScreen = findViewById(R.id.clMainScreen);
        mBtnCheckSchedules = findViewById(R.id.btnCheckSchedules);
        mBtnBuyTickets = findViewById(R.id.btnBuyTickets);
        mIvExpoSellerLogo1 = findViewById(R.id.ivExpoSellerLogo1);
        mGlHorizontalSeparator1 = findViewById(R.id.glHorizontalSeparator1);
        mGlHorizontalSeparator2 = findViewById(R.id.glHorizontalSeparator2);
        mIvAdminLogin = findViewById(R.id.ivAdminLogin);
        mClMainUserScreen = findViewById(R.id.clMainUserScreen);
        mFgAdminLogin = findViewById(R.id.fgAdminLogin);

        // Set the visibility of login form to gone
        mFgAdminLogin.setVisibility(View.GONE);

        // Set the button buy callback
        mBtnBuyTickets.setOnClickListener(v -> {
            // Start the activity Buy Tickets
            startActivity(new Intent(MainScreenActivity.this, BuyTicketsActivity.class));

            // Stop the idle time
            TimeOutIdle.stopIdleHandler();

            // Finish the activity
            finish();
        });

        // Set the button check schedule callback
        mBtnCheckSchedules.setOnClickListener(v -> {
            // Start the activity Check Schedules
            startActivity(new Intent(MainScreenActivity.this, CheckSchedulesActivity.class));

            // Stop the idle time
            TimeOutIdle.stopIdleHandler();

            // Finish the activity
            finish();
        });

        // Set the button admin login
        mIvAdminLogin.setOnClickListener(v -> {
            // Configure the animation
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            mLoadAnimation.setDuration(1000);

            // Set visible the panel
            mFgAdminLogin.setVisibility(View.VISIBLE);
            mFgAdminLogin.startAnimation(mLoadAnimation);
        });
    }

    /**
     * Method to detect if back pressed
     */
    @Override
    public void onBackPressed() {
        // If the login form is open, close it
        if (mFgAdminLogin.getVisibility() != View.GONE) {
            // Configure the animation
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            mLoadAnimation.setDuration(1000);

            // Hidden the fragment
            mFgAdminLogin.setVisibility(View.GONE);
            mFgAdminLogin.startAnimation(mLoadAnimation);
        }

        // Inform via logcat of this is main application of the device, and exit the application is forbidden
        Log.w(ExpoSellerApplication.LOG_TAG, "Ignoring back button pressed because is a Main IoT Application...");
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