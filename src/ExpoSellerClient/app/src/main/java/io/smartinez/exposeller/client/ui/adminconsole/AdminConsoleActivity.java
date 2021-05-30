package io.smartinez.exposeller.client.ui.adminconsole;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist.AdsListFragment;
import io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist.ConcertsListFragment;
import io.smartinez.exposeller.client.util.FragmentUtils;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class AdminConsoleActivity extends AppCompatActivity {
    // Elements of the view
    private ConstraintLayout mClAdminConsole;
    private ConstraintLayout mClMainFrame;
    private ConstraintLayout mClLateralButtons;
    private Guideline mGlVerticalSeparator2;
    private FrameLayout mFlFragmentSurface;

    // Elements of concerts button
    private CardView mCvConcerts;
    private ConstraintLayout mClConcerts;
    private ImageView mIvConcerts;
    private TextView mTvConcerts;

    // Elements of advertisements button
    private CardView mCvAds;
    private ConstraintLayout mClAds;
    private ImageView mIvAds;
    private TextView mTvAds;

    // Elements of system settings button
    private CardView mCvSystemSettings;
    private ConstraintLayout mClSystemSettings;
    private ImageView mIvSystemSettings;
    private TextView mTvSystemSettings;

    // Elements of exit button
    private CardView mCvExit;
    private ConstraintLayout mClExit;
    private ImageView mIvExit;
    private TextView mTvExit;

    // Instance of view model
    private AdminConsoleViewModel mViewModel;

    /**
     * Method of inflate the view and initialize the elements
     *
     * @param savedInstanceState The bundle with saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call to parent method
        super.onCreate(savedInstanceState);

        // Inflate the view
        setContentView(R.layout.activity_admin_console);

        // Stop the idle timer
        TimeOutIdle.stopIdleHandler();

        // Init the view elements
        initView();

        // Perform a click into concerts list
        mCvConcerts.performClick();
    }

    /**
     * Private method to initialize the view
     */
    private void initView() {
        // Bind the view elements
        mClAdminConsole = findViewById(R.id.clAdminConsole);
        mClMainFrame = findViewById(R.id.clMainFrame);
        mClLateralButtons = findViewById(R.id.clLateralButtons);
        mGlVerticalSeparator2 = findViewById(R.id.glVerticalSeparator2) ;
        mFlFragmentSurface = findViewById(R.id.flFragmentSurface);

        // Bind the concert button elements
        mCvConcerts = findViewById(R.id.cvConcerts);
        mClConcerts = findViewById(R.id.clConcerts);
        mIvConcerts = findViewById(R.id.ivConcerts);
        mTvConcerts = findViewById(R.id.tvConcerts);

        // Bind the advertisement button elements
        mCvAds = findViewById(R.id.cvAds);
        mClAds = findViewById(R.id.clAds);
        mIvAds = findViewById(R.id.ivAds);
        mTvAds = findViewById(R.id.tvAds);

        // Bind the system settings button elements
        mCvSystemSettings = findViewById(R.id.cvSystemSettings);
        mClSystemSettings = findViewById(R.id.clSystemSettings);
        mIvSystemSettings = findViewById(R.id.ivSystemSettings);
        mTvSystemSettings = findViewById(R.id.tvSystemSettings);

        // Bind the exit button elements
        mCvExit = findViewById(R.id.cvExit);
        mClExit = findViewById(R.id.clExit);
        mIvExit = findViewById(R.id.ivExit);
        mTvExit = findViewById(R.id.tvExit);

        // Instance the view model
        mViewModel = new ViewModelProvider(this).get(AdminConsoleViewModel.class);

        // Bind the callbacks to list buttons
        mCvConcerts.setOnClickListener(v -> FragmentUtils.interchangeFragement(getSupportFragmentManager(), R.id.flFragmentSurface, ConcertsListFragment.class));
        mCvAds.setOnClickListener(v -> FragmentUtils.interchangeFragement(getSupportFragmentManager(), R.id.flFragmentSurface, AdsListFragment.class));

        // Bind the system settings callback
        mCvSystemSettings.setOnClickListener(v -> mViewModel.openSystemSettings(AdminConsoleActivity.this));

        // Bind the exit button callback
        mCvExit.setOnClickListener(v -> AdminConsoleActivity.this.onBackPressed());
    }

    /**
     * Method to detect back pressed
     */
    @Override
    public void onBackPressed() {
        // Call to exit admin console procedure
        mViewModel.exitAdminConsole(AdminConsoleActivity.this, super::onBackPressed);
    }
}