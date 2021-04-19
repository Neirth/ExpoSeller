package io.smartinez.exposeller.client.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.checkschedules.CheckSchedulesActivity;
import io.smartinez.exposeller.client.util.TimeoutIdle;

@AndroidEntryPoint
public class MainScreenActivity extends AppCompatActivity {

    private ConstraintLayout mClMainScreen;
    private Button mBtnCheckSchedules;
    private Button mBtnBuyTickets;
    private ImageView mIvExpoSellerLogo1;
    private Guideline mGlHorizontalSeparator1;
    private Guideline mGlHorizontalSeparator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initView();

        TimeoutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(MainScreenActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeoutIdle.startIdleHandler();
    }

    public void initView() {
        mClMainScreen = findViewById(R.id.clMainScreen);
        mBtnCheckSchedules = findViewById(R.id.btnCheckSchedules);
        mBtnBuyTickets = findViewById(R.id.btnBuyTickets);
        mIvExpoSellerLogo1 = findViewById(R.id.ivExpoSellerLogo1);
        mGlHorizontalSeparator1 = findViewById(R.id.glHorizontalSeparator1);
        mGlHorizontalSeparator2 = findViewById(R.id.glHorizontalSeparator2);

        mBtnBuyTickets.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, BuyTicketsActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        mBtnCheckSchedules.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, CheckSchedulesActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    public void onBackPressed() {
        Log.d(ExpoSellerApplication.LOG_TAG, "Ignoring back button pressed because is a Main IoT Application...");
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        TimeoutIdle.stopIdleHandler();
        TimeoutIdle.startIdleHandler();
    }
}