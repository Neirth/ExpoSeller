package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.service.AdminService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@HiltViewModel
public class ConcertsMgtViewModel extends ViewModel {
    // Instance of use cases layer
    private final AdminService mAdminService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    @Inject
    public ConcertsMgtViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to get the instance of concert repo
     *
     * @return The instance of concert repo
     */
    public ConcertRepo getConcertRepo() {
        return mAdminService.getConcertRepo();
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
        return mAdminService.generateFriendlyId(Concert.class);
    }
}