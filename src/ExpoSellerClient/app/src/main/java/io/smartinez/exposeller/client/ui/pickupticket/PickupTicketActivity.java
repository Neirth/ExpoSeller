package io.smartinez.exposeller.client.ui.pickupticket;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.peripherals.ticketgenerator.TicketType;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
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
    private TextView mTvPrintingTicket;

    private String mUriTicket;
    private PickupTicketViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_ticket);

        mUriTicket = getIntent().getStringExtra(EXTRA_TICKET);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(PickupTicketActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            finish();
        });

        TimeOutIdle.startIdleHandler(30 * 1000);
    }

    private void initView() {
        mClPickupTicket = findViewById(R.id.clPickupTicket);
        mIvPickupTicket = findViewById(R.id.ivPickupTicket);
        mTvPickupTicket = findViewById(R.id.tvPickupTicket);
        mIvExpoSellerLogo4 = findViewById(R.id.ivExpoSellerLogo4);
        mIvQrCode = findViewById(R.id.ivQrCode);
        mTvPickupText1 = findViewById(R.id.tvPickupText1);
        mTvPickupText2 = findViewById(R.id.tvPickupText2);
        mTvPrintingTicket = findViewById(R.id.tvPrintingTicket);

        mViewModel = new ViewModelProvider(this).get(PickupTicketViewModel.class);

        if (mViewModel.getTicketImplType() == TicketType.VIRTUAL || mViewModel.getTicketImplType() == TicketType.HYBRID) {
            mViewModel.generateTicketQrCode(this, mIvQrCode, mUriTicket);
        } else if (mViewModel.getTicketImplType() == TicketType.PHYSICAL){
            mIvQrCode.setVisibility(View.GONE);
            mTvPrintingTicket.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PickupTicketActivity.this, MainScreenActivity.class);
        startActivity(intent);

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TimeOutIdle.stopIdleHandler();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimeOutIdle.stopIdleHandler();
        TimeOutIdle.startIdleHandler(30 * 1000);
    }
}