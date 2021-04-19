package io.smartinez.exposeller.client.ui.buytickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.adapter.BuyTicketConcertAdapter;
import io.smartinez.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.smartinez.exposeller.client.util.TimeoutIdle;

public class BuyTicketsActivity extends AppCompatActivity {
    private ConstraintLayout mClBuyTickets;
    private TextView mTvInsertImportValue2;
    private ImageView mIvExpoSellerLogo5;
    private RecyclerView mRvBuyTickets;

    private BuyTicketConcertAdapter mBuyTicketConcertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_tickets);

        initView();

        TimeoutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(BuyTicketsActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeoutIdle.startIdleHandler();
    }

    public void initView() {
        mClBuyTickets = findViewById(R.id.clBuyTickets);
        mTvInsertImportValue2 = findViewById(R.id.tvInsertImportValue2);
        mIvExpoSellerLogo5 = findViewById(R.id.ivExpoSellerLogo5);
        mRvBuyTickets = findViewById(R.id.rvBuyTickets);

        mBuyTicketConcertAdapter = new BuyTicketConcertAdapter();
        mBuyTicketConcertAdapter.setAdapterClickListener(model -> {
            if (model instanceof Concert) {
                Concert concert = (Concert) model;

                Intent intent = new Intent(BuyTicketsActivity.this, InsertCoinsActivity.class);
                intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert);

                startActivity(intent);
            }
        });

        mRvBuyTickets.setAdapter(mBuyTicketConcertAdapter);
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

        TimeoutIdle.stopIdleHandler();
        TimeoutIdle.startIdleHandler();
    }
}