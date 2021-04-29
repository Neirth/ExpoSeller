package io.smartinez.exposeller.client.ui.mainscreen;

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
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.checkschedules.CheckSchedulesActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class MainScreenActivity extends AppCompatActivity {
    private ConstraintLayout mClMainScreen;
    private Button mBtnCheckSchedules;
    private Button mBtnBuyTickets;
    private ImageView mIvExpoSellerLogo1;
    private Guideline mGlHorizontalSeparator1;
    private Guideline mGlHorizontalSeparator2;
    private View mFgAdminLogin;
    private ConstraintLayout mClMainUserScreen;
    private ImageView mIvAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(MainScreenActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    private void initView() {
        mClMainScreen = findViewById(R.id.clMainScreen);
        mBtnCheckSchedules = findViewById(R.id.btnCheckSchedules);
        mBtnBuyTickets = findViewById(R.id.btnBuyTickets);
        mIvExpoSellerLogo1 = findViewById(R.id.ivExpoSellerLogo1);
        mGlHorizontalSeparator1 = findViewById(R.id.glHorizontalSeparator1);
        mGlHorizontalSeparator2 = findViewById(R.id.glHorizontalSeparator2);
        mIvAdminLogin = findViewById(R.id.ivAdminLogin);
        mClMainUserScreen = findViewById(R.id.clMainUserScreen);
        mFgAdminLogin = findViewById(R.id.fgAdminLogin);

        mFgAdminLogin.setVisibility(View.GONE);

        mBtnBuyTickets.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, BuyTicketsActivity.class);
            startActivity(intent);

            TimeOutIdle.stopIdleHandler();
            finish();
        });

        mBtnCheckSchedules.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, CheckSchedulesActivity.class);
            startActivity(intent);

            TimeOutIdle.stopIdleHandler();
            finish();
        });

        mIvAdminLogin.setOnClickListener(v -> {
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            mLoadAnimation.setDuration(1000);

            mFgAdminLogin.setVisibility(View.VISIBLE);
            mFgAdminLogin.startAnimation(mLoadAnimation);
        });
    }

    @Override
    public void onBackPressed() {
        if (mFgAdminLogin.getVisibility() != View.GONE) {
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            mLoadAnimation.setDuration(1000);

            mFgAdminLogin.setVisibility(View.GONE);
            mFgAdminLogin.startAnimation(mLoadAnimation);
        }

        Log.d(ExpoSellerApplication.LOG_TAG, "Ignoring back button pressed because is a Main IoT Application...");
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