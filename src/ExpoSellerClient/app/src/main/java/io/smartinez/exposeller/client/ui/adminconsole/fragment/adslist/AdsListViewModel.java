package io.smartinez.exposeller.client.ui.adminconsole.fragment.adslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class AdsListViewModel extends ViewModel {
    private AdminService mAdminService;
    private ExecutorService mExecutorService;

    @Inject
    public AdsListViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    public AdBannerRepo getAdBannerRepo() {
        return mAdminService.getAdBannerRepo();
    }

    public void searchListAdBanners(AdminService.TypeSearch typeSearch, long parameter) {
        mExecutorService.submit(() -> {
            try {
                mAdminService.searchListModels(AdBanner.class, typeSearch, parameter);
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public void notifyListChanges() {
        mExecutorService.submit(() -> {
            try {
                mAdminService.notifyListChanges();
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<List<AdBanner>> getListAdBanner() {
        return mAdminService.getListAdBanner();
    }
}