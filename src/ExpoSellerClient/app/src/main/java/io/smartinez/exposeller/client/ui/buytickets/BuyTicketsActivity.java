package io.smartinez.exposeller.client.ui.buytickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.adapter.BuyTicketConcertAdapter;
import io.smartinez.exposeller.client.ui.insertcoins.InsertCoinsActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class BuyTicketsActivity extends AppCompatActivity {
    private ConstraintLayout mClBuyTickets;
    private TextView mTvInsertImportValue2;
    private ImageView mIvExpoSellerLogo5;
    private RecyclerView mRvBuyTickets;

    private BuyTicketConcertAdapter mAdapter;
    private BuyTicketsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_tickets);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(BuyTicketsActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    public void initView() {
        mClBuyTickets = findViewById(R.id.clBuyTickets);
        mTvInsertImportValue2 = findViewById(R.id.tvInsertImportValue2);
        mIvExpoSellerLogo5 = findViewById(R.id.ivExpoSellerLogo5);
        mRvBuyTickets = findViewById(R.id.rvBuyTickets);

        mViewModel = new ViewModelProvider(this).get(BuyTicketsViewModel.class);

        mAdapter = new BuyTicketConcertAdapter();
        mAdapter.setAdapterClickListener(model -> {
            if (model instanceof Concert) {
                Concert concert = (Concert) model;

                Intent intent = new Intent(BuyTicketsActivity.this, InsertCoinsActivity.class);
                intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert);

                startActivity(intent);

                finish();
            }
        });

        mRvBuyTickets.setAdapter(mAdapter);
        mViewModel.readConcertList().observe(this, value -> mAdapter.setConcertList(value));
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