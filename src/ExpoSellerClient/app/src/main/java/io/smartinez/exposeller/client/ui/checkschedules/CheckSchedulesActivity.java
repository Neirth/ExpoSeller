package io.smartinez.exposeller.client.ui.checkschedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.checkschedules.adapter.CheckSchedulesAdapter;
import io.smartinez.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

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

    private CheckSchedulesAdapter mConcertScheduleAdapter;
    private CheckSchedulesViewModel mConcertScheduleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_schedules);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(CheckSchedulesActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    public void initView() {
        mClCheckSchedules = findViewById(R.id.clCheckSchedules);
        mGlVerticalSeparator = findViewById(R.id.glVerticalSeparator);
        mRvConcertList = findViewById(R.id.rvConcertList);
        mGlHorizontalSeparator = findViewById(R.id.glHorizontalSeparator);
        mIvExpoSellerLogo2 = findViewById(R.id.ivExpoSellerLogo2);
        mTvTitleCheckSchedules = findViewById(R.id.tvTitleCheckSchedules);
        mBtnCancelOperation1 = findViewById(R.id.btnCancelOperation1);
        mClCalendar = findViewById(R.id.clCalendar);
        mCvCalendar = findViewById(R.id.cvCalendar);

        mBtnCancelOperation1.setOnClickListener(v -> CheckSchedulesActivity.this.onBackPressed());
        mConcertScheduleViewModel = new ViewModelProvider(this).get(CheckSchedulesViewModel.class);

        mConcertScheduleAdapter = new CheckSchedulesAdapter();
        mConcertScheduleAdapter.setOnAdapterClickListener(model -> {
            if (model instanceof Concert) {
                Concert concert = (Concert) model;

                Intent intent = new Intent(CheckSchedulesActivity.this, InsertCoinsActivity.class);
                intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert);

                startActivity(intent);

                finish();
            }
        });

        mRvConcertList.setAdapter(mConcertScheduleAdapter);

        mCvCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String monthStr = Month.of(month + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();

            mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, year));

            mConcertScheduleViewModel.searchConcertWithDay(new Date(view.getDate()))
                                     .observe(this, concerts -> mConcertScheduleAdapter.setConcertList(concerts));
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String monthStr = Month.of(calendar.get(Calendar.MONTH) + 1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()).toLowerCase();
        Integer yearInt = calendar.get(Calendar.YEAR);

        mTvTitleCheckSchedules.setText(String.format(getString(R.string.check_schedules), monthStr, yearInt));

        mConcertScheduleViewModel.searchConcertWithDay(new Date())
                                 .observe(this, concerts -> mConcertScheduleAdapter.setConcertList(concerts));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        TimeOutIdle.stopIdleHandler();
        TimeOutIdle.startIdleHandler();
    }
}