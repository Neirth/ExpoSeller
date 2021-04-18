package io.smartinez.exposeller.client.ui.mainscreen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;

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
    }

    public void initView() {
        mClMainScreen = findViewById(R.id.clMainScreen);
        mBtnCheckSchedules = findViewById(R.id.btnCheckSchedules);
        mBtnBuyTickets = findViewById(R.id.btnBuyTickets);
        mIvExpoSellerLogo1 = findViewById(R.id.ivExpoSellerLogo1);
        mGlHorizontalSeparator1 = findViewById(R.id.glHorizontalSeparator1);
        mGlHorizontalSeparator2 = findViewById(R.id.glHorizontalSeparator2);
    }
}