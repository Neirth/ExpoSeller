package io.smartinez.exposeller.client.ui.buytickets;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;
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
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class BuyTicketsActivity extends AppCompatActivity {
    private ConstraintLayout mClBuyTickets;
    private TextView mTvInsertImportValue2;
    private ImageView mIvExpoSellerLogo5;
    private RecyclerView mRvBuyTickets;
    private TextView mTvNotFoundConcerts2;

    private BuyTicketConcertAdapter mAdapter;
    private BuyTicketsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_tickets);

        initView();
        initRecyclerView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(BuyTicketsActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            finish();
        });

        TimeOutIdle.startIdleHandler();

        mClBuyTickets.sendAccessibilityEvent(AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION);
    }

    private void initView() {
        mClBuyTickets = findViewById(R.id.clBuyTickets);
        mTvInsertImportValue2 = findViewById(R.id.tvInsertImportValue2);
        mIvExpoSellerLogo5 = findViewById(R.id.ivExpoSellerLogo5);
        mRvBuyTickets = findViewById(R.id.rvBuyTickets);
        mTvNotFoundConcerts2 = findViewById(R.id.tvNotFoundConcerts2);

        mViewModel = new ViewModelProvider(this).get(BuyTicketsViewModel.class);
    }

    private void initRecyclerView() {
        mAdapter = new BuyTicketConcertAdapter();
        mAdapter.setAdapterClickListener(model -> {
            if (model instanceof Concert) {
                Concert concert = (Concert) model;

                Intent intent = new Intent(BuyTicketsActivity.this, InsertCoinsActivity.class);
                startActivity(intent.putExtra(InsertCoinsActivity.EXTRA_CONCERT, concert));

                finish();
            }
        });

        mRvBuyTickets.setAdapter(mAdapter);
        mViewModel.readConcertList().observe(this, value -> {
            if (value.size() >= 1) {
                mTvNotFoundConcerts2.setVisibility(View.GONE);
            } else {
                mTvNotFoundConcerts2.setVisibility(View.VISIBLE);
            }

            mAdapter.setConcertList(value);
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BuyTicketsActivity.this, MainScreenActivity.class);
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
        TimeOutIdle.startIdleHandler();
    }
}