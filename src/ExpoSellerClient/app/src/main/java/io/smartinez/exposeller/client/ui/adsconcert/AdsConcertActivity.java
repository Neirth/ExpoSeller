package io.smartinez.exposeller.client.ui.adsconcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;

public class AdsConcertActivity extends AppCompatActivity {

    private ConstraintLayout mClAdsConcert;
    private ImageView mIvAdsConcertView;

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