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
    // Instance of use cases layer
    private final AdminService mAdminService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    @Inject
    public AdsMgtViewModel(AdminService adminService, ExecutorService executorService) {
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
     * Method to get the instance of executor service
     *
     * @return The instance of executor service
     */
    public ExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    /**
     * Method to generate the friendly id
     *
     * @return The friendly generated
     * @throws IOException In case of not being able to access the repository
     */
    public int generateFriendlyId() throws IOException {
        return mAdminService.generateFriendlyId(Advertisement.class);
    }
}