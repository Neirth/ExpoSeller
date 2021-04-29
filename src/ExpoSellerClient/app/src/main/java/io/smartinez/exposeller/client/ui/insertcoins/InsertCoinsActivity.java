package io.smartinez.exposeller.client.ui.insertcoins;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.ui.adsconcert.AdsConcertActivity;
import io.smartinez.exposeller.client.ui.buytickets.BuyTicketsActivity;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.smartinez.exposeller.client.ui.pickupticket.PickupTicketActivity;
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

    private Concert mConcert;
    private InsertCoinsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_coins);

        mConcert = getIntent().getParcelableExtra(EXTRA_CONCERT);

        initView();

        TimeOutIdle.initIdleHandler(() -> {
            Intent intent = new Intent(InsertCoinsActivity.this, AdsConcertActivity.class);
            startActivity(intent);

            finish();
        });

        TimeOutIdle.startIdleHandler();
    }

    private void initView() {
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

        mTvConcertValue.setText(String.format(Locale.getDefault(), getString(R.string.concert_value), mConcert.getCost()));
        mTvConcertChoice.setText(String.format(Locale.getDefault(), getString(R.string.concert_choice), mConcert.getName()));
        mTvInsertedValue.setText(String.format(Locale.getDefault(), getString(R.string.inserted_value), 0.0));
        mTvReturnValue.setText(String.format(Locale.getDefault(), getString(R.string.return_value), 0.0));

        mViewModel = new ViewModelProvider(this).get(InsertCoinsViewModel.class);
        mViewModel.buyTicket(this, mConcert, mTvInsertedValue, mTvReturnValue)
            .thenApply(uriTicket -> {
                mBtnCancelOperation2.setClickable(false);

                runOnUiThread(() -> {
                    Intent intent = new Intent(InsertCoinsActivity.this, PickupTicketActivity.class);
                    startActivity(intent.putExtra(PickupTicketActivity.EXTRA_TICKET, uriTicket));

                    finish();
                });

                return null;
            }).exceptionally(ex -> {
                runOnUiThread(() -> {
                    try {
                        Toast toast = Toast.makeText(this, R.string.buy_error, Toast.LENGTH_SHORT);
                        toast.getView().setBackgroundColor(getResources().getColor(R.color.darken_grey_transparent));

                        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
                        toastMessage.setTextColor(Color.WHITE);

                        toast.show();

                        mViewModel.cancelBuyTicket(null);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });

                return null;
            });
    }

    @Override
    public void onBackPressed() {
        try {
            mViewModel.cancelBuyTicket(null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            super.onBackPressed();

            Intent intent = new Intent(InsertCoinsActivity.this, MainScreenActivity.class);
            startActivity(intent);
        }
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