package io.smartinez.exposeller.client.ui.adsconcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;

import io.smartinez.exposeller.client.R;

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
    }
}