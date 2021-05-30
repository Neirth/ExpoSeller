package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.repository.AdvertisementRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class AdsListViewModel extends ViewModel {
    // Instance of use cases layer
    private final AdminService mAdminService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    @Inject
    public AdsListViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to get the instance of advertisement repo
     *
     * @return The instance of advertisement repo
     */
    public AdvertisementRepo getAdvertisementRepo() {
        return mAdminService.getAdvertisementRepo();
    }

    /**
     * Method to get a live data with list of entities
     *
     * @return LiveData with List of Entities
     */
    public LiveData<List<Advertisement>> getListAdBanner() {
        return mAdminService.getAdvertisementList();
    }

    /**
     * Method to search with parameters in the repo
     *
     * @param typeSearch The type of search
     * @param parameter The parameter to search
     */
    public void searchListAdBanners(AdminService.TypeSearch typeSearch, long parameter) {
        mExecutorService.submit(() -> {
            try {
                mAdminService.searchListModels(Advertisement.class, typeSearch, parameter);
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method to notify changes in list
     */
    public void notifyListChanges() {
        mExecutorService.submit(() -> {
            try {
                mAdminService.notifyListChanges();
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}