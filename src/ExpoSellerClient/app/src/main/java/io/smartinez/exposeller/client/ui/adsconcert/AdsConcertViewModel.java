package io.smartinez.exposeller.client.ui.adsconcert;

import android.app.Activity;
import android.widget.ImageView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import com.bumptech.glide.Glide;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class AdsConcertViewModel extends ViewModel {
    // Instance for use case user
    private final UserService mUsersService;

    // Instance for executor service
    private final ExecutorService mExecutorService;

    // Atomic Reference to list advertisement
    private final AtomicReference<List<Advertisement>> mListAdvertisement;

    @Inject
    public AdsConcertViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
        this.mListAdvertisement = new AtomicReference<>(Collections.emptyList());
    }

    /**
     * Method for pick random list of advertisements
     *
     * @return LiveDate for Advertisement list
     */
    public LiveData<List<Advertisement>> pickRandomAdsList() {
        // Initialize the mutable live data
        MutableLiveData<List<Advertisement>> mutableAd = new MutableLiveData<>();

        // Execute in background the query
        mExecutorService.execute(() -> {
            try {
                mutableAd.postValue(mUsersService.pickRandomAdsList());
            } catch (IOException e) {
                mutableAd.postValue(Collections.emptyList());
            }
        });

        // Return the live data
        return mutableAd;
    }

    /**
     * Method to run in idle advertisement
     *
     * @param activity Activity instance
     * @param adImageView Image view to operate
     */
    public void runIdleAdvertisment(Activity activity, ImageView adImageView) {
        mExecutorService.execute(() -> {
            // Get thread object
            Thread currentThread = Thread.currentThread();

            while (true) {
                // Foreach the list
                mListAdvertisement.get().forEach(advertisement -> {
                    try {
                        // Check if not the activity is destroyed
                        if (!activity.isDestroyed() || !activity.isFinishing()) {
                            // Run in activity thread
                            activity.runOnUiThread(() -> {
                                // Set description for text to speech
                                adImageView.setContentDescription(advertisement.getName());

                                // Load the image from
                                Glide.with(activity).load(advertisement.getPhotoAd()).into(adImageView);
                            });

                            // Wait 30 seconds before iterate
                            Thread.sleep(30 * 1000);
                        } else {
                            // Stop the thread if is stopped the activity
                            currentThread.interrupt();
                        }
                    } catch (InterruptedException ignored) {
                    }
                });
            }
        });
    }

    /**
     * Method for get the list of advertisements
     *
     * @return The list of advertisements
     */
    public List<Advertisement> getListAdvertisement() {
        return mListAdvertisement.get();
    }

    /**
     * Method for set the list of advertisements
     * 
     * @param advertisementList The list of advertisements
     */
    public void setListAdvertisement(List<Advertisement> advertisementList) {
        mListAdvertisement.set(advertisementList);
    }
}
