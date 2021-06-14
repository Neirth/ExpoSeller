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
package io.neirth.exposeller.client.ui.checkschedules;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.neirth.exposeller.client.R;
import io.neirth.exposeller.client.domain.Concert;
import io.neirth.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.neirth.exposeller.client.ui.checkschedules.adapter.CheckSchedulesAdapter;
import io.neirth.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.neirth.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.neirth.exposeller.client.util.TimeOutIdle;
import io.neirth.exposeller.client.util.TimeUtils;
import io.neirth.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class CheckSchedulesActivity extends AppCompatActivity {
    // Elements of view
    private ConstraintLayout mClCheckSchedules;
    private Guideline mGlVerticalSeparator;
    private RecyclerView mRvConcertList;
    private Guideline mGlHorizontalSeparator;
    private ImageView mIvExpoSellerLogo2;
    private TextView mTvTitleCheckSchedules;
    private Button mBtnCancelOperation1;
    private ConstraintLayout mClCalendar;
    private CalendarView mCvCalendar;
    private TextView mTvNotFoundConcerts1;

    // Instance of adapter
    private CheckSchedulesAdapter mAdapter;

    // Instance of view model
    private CheckSchedulesViewModel mViewModel;

    // Calendar instance
    private Calendar mCalendar = Calendar.getInstance();

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
        setContentView(R.layout.activity_check_schedules);

        // Initialize the view
        initView();
        initRecyclerView();

        // Prepare the timeout idle
        TimeOutIdle.initIdleHandler(() -> {
            // Start a new activity
            startActivity(new Intent(CheckSchedulesActivity.this, AdsConcertActivity.class));

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
        mClCheckSchedules = findViewById(R.id.clCheckSchedules);
        mGlVerticalSeparator = findViewById(R.id.glVerticalSeparator);
        mRvConcertList = findViewById(R.id.rvConcertList);
        mGlHorizontalSeparator = findViewById(R.id.glHorizontalSeparator);
        mIvExpoSellerLogo2 = findViewById(R.id.ivExpoSellerLogo2);
        mTvTitleCheckSchedules = findViewById(R.id.tvTitleCheckSchedules);
        mBtnCancelOperation1 = findViewById(R.id.btnCancelOperation1);
        mClCalendar = findViewById(R.id.clCalendar);
        mCvCalendar = findViewById(R.id.cvCalendar);
        mTvNotFoundConcerts1 = findViewById(R.id.tvNotFoundConcerts1);

        // Bind the cancellation callback
        mBtnCancelOperation1.setOnClickListener(v -> CheckSchedulesActivity.this.onBackPressed());

        // Instance a view model
        mViewModel = new ViewModelProvider(this).get(CheckSchedulesViewModel.class);
    }

    /**
     * Private method to init the recycler view
     */
    private void initRecyclerView() {
        // Instance the adapter
        mAdapter = new CheckSchedulesAdapter();

        // Set callback to adapter
        mAdapter.setOnAdapterClickListener(model -> {
            if (model instanceof Concert) {
                // Cast the entity
                Concert concert = (Concert) model;

                // Get the epoch actual time
                long actualTime = (new Date()).getTime();

                // Get the epoch entity time
                long concertTime = concert.getEventDate().getTime();

                // Check if the entity is not in past time
                if (concertTime >= actualTime) {
                    // Open the activity with extra concert data
                    Intent intent = new Intent(CheckSchedulesActivity.this, InsertCoinsActivity.class);
                    startActivity(intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert));

                    // Finish actual activity
                    finish();
                } else {
                    // Show the toast with the error
                    Utilities.showToast(CheckSchedulesActivity.this, R.string.pass_time_calendar_error);
                }
            }
        });

        // Set the adapter to recycler view
        mRvConcertList.setAdapter(mAdapter);

        // Prepare the callback to calendar change
        Observer<List<Concert>> calendarObservable = concert -> {
            // Detect if the concert list is empty or not
            if (concert.size() >= 1) {
                mTvNotFoundConcerts1.setVisibility(View.GONE);
            } else {
                mTvNotFoundConcerts1.setVisibility(View.VISIBLE);
            }

            // Set the concert list
            mAdapter.setConcertList(concert);
        };

        // Bind the callback with calendar view
        mCvCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Get the month in string format
            String monthStr = Month.of(month + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();

            // Set the values into calendar instance
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Set the text into title of view
            mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, year));

            // Run the query
            mViewModel.searchConcertWithDay(mCalendar.getTime()).observe(this, calendarObservable);
        });

        // Get the month in string format
        String monthStr = Month.of(mCalendar.get(Calendar.MONTH) + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();

        // Get the year number
        Integer yearInt = mCalendar.get(Calendar.YEAR);

        // Set the text into title of view
        mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, yearInt));

        // Run the query
        mViewModel.searchConcertWithDay(TimeUtils.removeTimeFromDate(mCalendar.getTime())).observe(this, calendarObservable);
    }

    /**
     * Method to detect if back pressed
     */
    @Override
    public void onBackPressed() {
        // Start a new activity
        startActivity(new Intent(CheckSchedulesActivity.this, MainScreenActivity.class));

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