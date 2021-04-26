package io.smartinez.exposeller.client.ui.pickupticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class PickupTicketActivity extends AppCompatActivity {
    public static final String EXTRA_TICKET = "io.smartinez.exposeller.client.ui.pickupticket.ticketData";

    private ConstraintLayout mClPickupTicket;
    private ImageView mIvPickupTicket;
    private TextView mTvPickupTicket;
    private ImageView mIvExpoSellerLogo4;
    private ImageView mIvQrCode;
    private TextView mTvPickupText1;
    private TextView mTvPickupText2;

    private Uri mUriTicket;
    private PickupTicketViewModel mPickupTicketViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_ticket);

        mUriTicket = getIntent().getParcelableExtra(EXTRA_TICKET);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(PickupTicketActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    public void initView() {
        mClPickupTicket = findViewById(R.id.clPickupTicket);
        mIvPickupTicket = findViewById(R.id.ivPickupTicket);
        mTvPickupTicket = findViewById(R.id.tvPickupTicket);
        mIvExpoSellerLogo4 = findViewById(R.id.ivExpoSellerLogo4);
        mIvQrCode = findViewById(R.id.ivQrCode);
        mTvPickupText1 = findViewById(R.id.tvPickupText1);
        mTvPickupText2 = findViewById(R.id.tvPickupText2);

        mPickupTicketViewModel.generateTicketQrCode(this, mIvQrCode, mUriTicket);
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
        TimeOutIdle.startIdleHandler();
        TimeOutIdle.stopIdleHandler();
    }
}