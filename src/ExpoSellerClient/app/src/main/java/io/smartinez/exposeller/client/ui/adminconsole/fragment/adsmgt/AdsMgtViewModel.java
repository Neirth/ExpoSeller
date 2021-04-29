package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Advertisement;
import io.smartinez.exposeller.client.service.AdminService;
import io.smartinez.exposeller.client.repository.AdvertisementRepo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@HiltViewModel
public class AdsMgtViewModel extends ViewModel {
    private final AdminService mAdminService;
    private final ExecutorService mExecutorService;

    @Inject
    public AdsMgtViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    public AdvertisementRepo getAdBannerRepo() {
        return mAdminService.getAdBannerRepo();
    }

    public ExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    public int generateTicketFriendlyId() throws IOException {
        return mAdminService.generateTicketFriendlyId(Advertisement.class);
    }
}