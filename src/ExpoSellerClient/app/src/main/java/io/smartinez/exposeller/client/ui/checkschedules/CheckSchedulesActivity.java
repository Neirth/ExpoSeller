package io.smartinez.exposeller.client.ui.checkschedules;

import android.graphics.Color;
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
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.checkschedules.adapter.CheckSchedulesAdapter;
import io.smartinez.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;
import io.smartinez.exposeller.client.util.TimeUtils;
import io.smartinez.exposeller.client.util.Utilities;

@AndroidEntryPoint
public class CheckSchedulesActivity extends AppCompatActivity {
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

    private CheckSchedulesAdapter mAdapter;
    private CheckSchedulesViewModel mViewModel;

    private Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_schedules);

        initView();
        initRecyclerView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(CheckSchedulesActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    private void initView() {
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

        mBtnCancelOperation1.setOnClickListener(v -> CheckSchedulesActivity.this.onBackPressed());
        mViewModel = new ViewModelProvider(this).get(CheckSchedulesViewModel.class);
    }

    private void initRecyclerView() {
        mAdapter = new CheckSchedulesAdapter();
        mAdapter.setOnAdapterClickListener(model -> {
            if (model instanceof Concert) {
                Concert concert = (Concert) model;
                long actualTime = (new Date()).getTime();
                long concertTime = concert.getEventDate().getTime();

                if (concertTime >= actualTime) {
                    Intent intent = new Intent(CheckSchedulesActivity.this, InsertCoinsActivity.class);
                    startActivity(intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert));

                    finish();
                } else {
                    Toast toast = Toast.makeText(this, R.string.pass_time_calendar_error, Toast.LENGTH_SHORT);
                    toast.getView().setBackgroundColor(getResources().getColor(R.color.darken_grey_transparent));

                    TextView toastMessage = toast.getView().findViewById(android.R.id.message);
                    toastMessage.setTextColor(Color.WHITE);

                    toast.show();
                }
            }
        });

        mRvConcertList.setAdapter(mAdapter);

        Observer<List<Concert>> calendarObservable = concert -> {
            if (concert.size() >= 1) {
                mTvNotFoundConcerts1.setVisibility(View.GONE);
            } else {
                mTvNotFoundConcerts1.setVisibility(View.VISIBLE);
            }

            mAdapter.setConcertList(concert);
        };

        mCvCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String monthStr = Month.of(month + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();

            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, year));

            mViewModel.searchConcertWithDay(mCalendar.getTime()).observe(this, calendarObservable);
        });

        mCalendar.setTime(new Date());

        String monthStr = Month.of(mCalendar.get(Calendar.MONTH) + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();
        Integer yearInt = mCalendar.get(Calendar.YEAR);

        mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, yearInt));
        mViewModel.searchConcertWithDay(TimeUtils.removeTimeFromDate(mCalendar.getTime())).observe(this, calendarObservable);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckSchedulesActivity.this, MainScreenActivity.class);
        startActivity(intent);

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeOutIdle.stopIdleHandler();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        TimeOutIdle.stopIdleHandler();
        TimeOutIdle.startIdleHandler();
    }
}