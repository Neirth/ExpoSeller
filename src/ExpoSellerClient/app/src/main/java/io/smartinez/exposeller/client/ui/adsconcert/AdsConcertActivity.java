package io.smartinez.exposeller.client.ui.adsconcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;

@AndroidEntryPoint
public class AdsConcertActivity extends AppCompatActivity {
    private ConstraintLayout mClAdsConcert;
    private ImageView mIvAdsConcertView;

    private AdsConcertViewModel mAdsConcertViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_concert);

        initView();
    }

    public void initView() {
        mClAdsConcert = findViewById(R.id.clAdsConcert);
        mIvAdsConcertView = findViewById(R.id.ivAdsConcertView);

        mIvAdsConcertView.setOnClickListener(v -> AdsConcertActivity.this.onBackPressed());
        mAdsConcertViewModel = new ViewModelProvider(this).get(AdsConcertViewModel.class);

        mAdsConcertViewModel.pickRandomAdsList().observe(this, value -> {
            while (true) {
                value.forEach(adBanner -> {
                    try {
                        Glide.with(this).load(adBanner.getPhotoAd()).into(mIvAdsConcertView);

                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AdsConcertActivity.this, MainScreenActivity.class);
        startActivity(intent);

        overridePendingTransition(0, android.R.anim.fade_out);

        finish();
    }
}