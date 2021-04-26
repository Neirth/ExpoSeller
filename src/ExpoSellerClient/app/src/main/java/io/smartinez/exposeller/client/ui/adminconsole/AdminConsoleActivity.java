package io.smartinez.exposeller.client.ui.adminconsole;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import io.smartinez.exposeller.client.R;

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

    private View mFgAdsList;
    private View mFgUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);

        initView();
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

        mFgAdsList = findViewById(R.id.fgAdsList);
        mFgUserList = findViewById(R.id.fgUserList);
    }

}