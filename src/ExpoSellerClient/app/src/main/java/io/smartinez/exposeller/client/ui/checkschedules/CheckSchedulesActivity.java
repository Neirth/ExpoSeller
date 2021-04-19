package io.smartinez.exposeller.client.ui.checkschedules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.util.TimeoutIdle;

public class CheckSchedulesActivity extends AppCompatActivity {

    private ConstraintLayout mClCheckSchedules;
    private Guideline mGlVerticalSeparator;
    private RecyclerView mRvConcertList;
    private Guideline mGlHorizontalSeparator;
    private ImageView mIvExpoSellerLogo2;
    private TextView mTvTitlePickup;
    private Button mBtnCancelOperation1;
    private ConstraintLayout mClCalendar;
    private CalendarView mCvCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_schedules);

        initView();

        TimeoutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(CheckSchedulesActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeoutIdle.startIdleHandler();
    }

    public void initView() {
        mClCheckSchedules = findViewById(R.id.clCheckSchedules);
        mGlVerticalSeparator = findViewById(R.id.glVerticalSeparator);
        mRvConcertList = findViewById(R.id.rvConcertList);
        mGlHorizontalSeparator = findViewById(R.id.glHorizontalSeparator);
        mIvExpoSellerLogo2 = findViewById(R.id.ivExpoSellerLogo2);
        mTvTitlePickup = findViewById(R.id.tvTitlePickup);
        mBtnCancelOperation1 = findViewById(R.id.btnCancelOperation1);
        mClCalendar = findViewById(R.id.clCalendar);
        mCvCalendar = findViewById(R.id.cvCalendar);

        mBtnCancelOperation1.setOnClickListener(v -> CheckSchedulesActivity.this.onBackPressed());
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

        TimeoutIdle.stopIdleHandler();
        TimeoutIdle.startIdleHandler();
    }
}