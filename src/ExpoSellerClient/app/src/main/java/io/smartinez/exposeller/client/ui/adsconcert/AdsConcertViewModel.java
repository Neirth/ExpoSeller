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
    private final UserService mUsersService;
    private final ExecutorService mExecutorService;
    private final AtomicReference<List<Advertisement>> mListAdvertisement;

    @Inject
    public AdsConcertViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
        this.mListAdvertisement = new AtomicReference<>(Collections.emptyList());
    }

    public LiveData<List<Advertisement>> pickRandomAdsList() {
        MutableLiveData<List<Advertisement>> mutableAd = new MutableLiveData<>();

        mExecutorService.execute(() -> {
            try {
                mutableAd.postValue(mUsersService.pickRandomAdsList());
            } catch (IOException e) {
                mutableAd.postValue(Collections.emptyList());
            }
        });

        return mutableAd;
    }

    public void runIdleAdvertisment(Activity activity, ImageView adImageView) {
        mExecutorService.execute(() -> {
            Thread currentThread = Thread.currentThread();

            while (true) {
                if (mListAdvertisement.get().size() >= 1) {
                    mListAdvertisement.get().forEach(adBanner -> {
                        try {
                            activity.runOnUiThread(() -> {
                                try {
                                    adImageView.setContentDescription(adBanner.getName());
                                    Glide.with(activity).load(adBanner.getPhotoAd()).into(adImageView);
                                } catch (IllegalArgumentException e) {
                                    currentThread.interrupt();
                                }
                            });

                            Thread.sleep(30 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    break;
                }
            }
        });
    }

    public List<Advertisement> getListAdvertisement() {
        return mListAdvertisement.get();
    }

    public void setListAdvertisement(List<Advertisement> advertisementList) {
        mListAdvertisement.set(advertisementList);
    }
}
