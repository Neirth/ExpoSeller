package io.smartinez.exposeller.client.ui.adsconcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.ui.mainscreen.MainScreenActivity;
import io.smartinez.exposeller.client.util.TimeOutIdle;

@AndroidEntryPoint
public class AdsConcertActivity extends AppCompatActivity {
    // Elements of the view
    private ConstraintLayout mClAdsConcert;
    private ImageView mIvAdsConcertView;

    // Instance of View Model
    private AdsConcertViewModel mViewModel;

    /**
     * Method to inflate the view
     *
     * @param savedInstanceState The bundle with saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call to parent constructor
        super.onCreate(savedInstanceState);

        // Inflate the view
        setContentView(R.layout.activity_ads_concert);

        // Initialize the view
        initView();
    }

    /**
     * Private method to initialize the view
     */
    private void initView() {
        // Bind the elements
        mClAdsConcert = findViewById(R.id.clAdsConcert);
        mIvAdsConcertView = findViewById(R.id.ivAdsConcertView);

        // Set callback for click
        mIvAdsConcertView.setOnClickListener(v -> AdsConcertActivity.this.onBackPressed());

        // Initialize the view model
        mViewModel = new ViewModelProvider(this).get(AdsConcertViewModel.class);

        // Pick random elements from list and observe it
        mViewModel.pickRandomAdsList().observe(this, value -> mViewModel.setListAdvertisement(value));

        // Run the idle advertisements
        mViewModel.runIdleAdvertisment(this, mIvAdsConcertView);
    }

    @Override
    public void onBackPressed() {
        // Start a new activity
        startActivity(new Intent(AdsConcertActivity.this, MainScreenActivity.class));

        // Call to parent method
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        // Call to parent method
        super.onPause();

        // Stop Idle time
        TimeOutIdle.stopIdleHandler();
    }
}