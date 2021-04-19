package io.smartinez.exposeller.client.ui.pickupticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.util.TimeoutIdle;

public class PickupTicketActivity extends AppCompatActivity {

    private ConstraintLayout mClPickupTicket;
    private ImageView mIvPickupTicket;
    private TextView mTvPickupTicket;
    private ImageView mIvExpoSellerLogo4;
    private ImageView mIvQrCode;
    private TextView mTvPickupText1;
    private TextView mTvPickupText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_ticket);

        initView();

        TimeoutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(PickupTicketActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeoutIdle.startIdleHandler();
    }

    public void initView() {
        mClPickupTicket = findViewById(R.id.clPickupTicket);
        mIvPickupTicket = findViewById(R.id.ivPickupTicket);
        mTvPickupTicket = findViewById(R.id.tvPickupTicket);
        mIvExpoSellerLogo4 = findViewById(R.id.ivExpoSellerLogo4);
        mIvQrCode = findViewById(R.id.ivQrCode);
        mTvPickupText1 = findViewById(R.id.tvPickupText1);
        mTvPickupText2 = findViewById(R.id.tvPickupText2);
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
        TimeoutIdle.startIdleHandler();
        TimeoutIdle.stopIdleHandler();
    }
}