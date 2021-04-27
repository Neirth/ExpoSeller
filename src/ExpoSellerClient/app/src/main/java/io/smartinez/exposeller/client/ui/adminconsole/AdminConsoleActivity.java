package io.smartinez.exposeller.client.ui.adminconsole;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.AdsListFragment;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListFragment;
import io.smartinez.exposeller.client.util.FragmentsUtils;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class AdminConsoleActivity extends AppCompatActivity {
    private ConstraintLayout mClAdminConsole;
    private ConstraintLayout mClMainFrame;
    private ConstraintLayout mClLateralButtons;

    private CardView mCvConcerts;
    private ConstraintLayout mClConcerts;
    private ImageView mIvConcerts;
    private TextView mTvConcerts;

    private CardView mCvAds;
    private ConstraintLayout mClAds;
    private ImageView mIvAds;
    private TextView mTvAds;

    private CardView mCvSystemSettings;
    private ConstraintLayout mClSystemSettings;
    private ImageView mIvSystemSettings;
    private TextView mTvSystemSettings;

    private CardView mCvExit;
    private ConstraintLayout mClExit;
    private ImageView mIvExit;
    private TextView mTvExit;

    private Guideline mGlVerticalSeparator2;

    private View mFlFragmentSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);

        TimeOutIdle.stopIdleHandler();

        initView();
        mCvConcerts.performClick();
    }

    private void initView() {
        mClAdminConsole = findViewById(R.id.clAdminConsole);
        mClMainFrame = findViewById(R.id.clMainFrame);
        mClLateralButtons = findViewById(R.id.clLateralButtons);

        mCvConcerts = findViewById(R.id.cvConcerts);
        mClConcerts = findViewById(R.id.clConcerts);
        mIvConcerts = findViewById(R.id.ivConcerts);
        mTvConcerts = findViewById(R.id.tvConcerts);

        mCvAds = findViewById(R.id.cvAds);
        mClAds = findViewById(R.id.clAds);
        mIvAds = findViewById(R.id.ivAds);
        mTvAds = findViewById(R.id.tvAds);

        mCvSystemSettings = findViewById(R.id.cvSystemSettings);
        mClSystemSettings = findViewById(R.id.clSystemSettings);
        mIvSystemSettings = findViewById(R.id.ivSystemSettings);
        mTvSystemSettings = findViewById(R.id.tvSystemSettings);

        mCvExit = findViewById(R.id.cvExit);
        mClExit = findViewById(R.id.clExit);
        mIvExit = findViewById(R.id.ivExit);
        mTvExit = findViewById(R.id.tvExit);

        mGlVerticalSeparator2 = findViewById(R.id.glVerticalSeparator2) ;
        mFlFragmentSurface = findViewById(R.id.flFragmentSurface);

        mCvConcerts.setOnClickListener(v -> FragmentsUtils.interchangeFragement(getSupportFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
        mCvAds.setOnClickListener(v -> FragmentsUtils.interchangeFragement(getSupportFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));

        mCvSystemSettings.setOnClickListener(v -> {
            try {
                startActivity(Intent.makeMainActivity(new ComponentName("com.android.iotlauncher", "com.android.iotlauncher.DefaultIoTLauncher")));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mCvExit.setOnClickListener(v -> AdminConsoleActivity.this.onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}