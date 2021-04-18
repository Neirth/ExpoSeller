package io.smartinez.exposeller.client.ui.insertcoins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.smartinez.exposeller.client.R;

public class InsertCoinsActivity extends AppCompatActivity {

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
    }
}