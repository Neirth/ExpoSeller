package io.smartinez.exposeller.client.ui.insertcoins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class InsertCoinsActivity extends AppCompatActivity {
    public static final String EXTRA_CONCERT = "io.smartinez.exposeller.client.ui.insertcoins.concertData";

    private ConstraintLayout mClInsertCoins;
    private ImageView mIvInsertCoins;
    private ImageView mIvExpoSellerLogo3;
    private TextView mTvInsertImportValue;
    private TextView mTvConcertChoice;
    private TextView mTvConcertValue;
    private TextView mTvInsertedValue;
    private TextView mTvReturnValue;
    private TextView mTvInsertCoins;
    private Button mBtnCancelOperation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_coins);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(InsertCoinsActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    public void initView() {
        mClInsertCoins = findViewById(R.id.clInsertCoins);
        mIvInsertCoins = findViewById(R.id.ivInsertCoins);
        mTvInsertImportValue = findViewById(R.id.tvInsertImportValue);
        mTvConcertChoice = findViewById(R.id.tvConcertChoice);
        mTvConcertValue = findViewById(R.id.tvConcertValue);
        mTvInsertedValue = findViewById(R.id.tvInsertedValue);
        mTvReturnValue = findViewById(R.id.tvReturnValue);
        mTvInsertCoins = findViewById(R.id.tvInsertCoins);
        mBtnCancelOperation2 = findViewById(R.id.btnCancelOperation2);

        mBtnCancelOperation2.setOnClickListener(v -> InsertCoinsActivity.this.onBackPressed());
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

        TimeOutIdle.stopIdleHandler();
        TimeOutIdle.startIdleHandler();
    }
}