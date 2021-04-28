package io.smartinez.exposeller.client.ui.adminconsole.fragment.adsmgt;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.repository.AdBannerRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class AdsMgtViewModel extends ViewModel {
    private AdminService mAdminService;

    @Inject
    public AdsMgtViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    public AdBannerRepo getAdBannerRepo() {
        return mAdminService.getAdBannerRepo();
    }

}