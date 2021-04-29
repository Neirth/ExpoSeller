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
    private final AdminService mAdminService;
    private final ExecutorService mExecutorService;

    @Inject
    public ConcertsMgtViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    public ConcertRepo getConcertRepo() {
        return mAdminService.getConcertRepo();
    }

    public ExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    public int generateTicketFriendlyId() throws IOException {
        return mAdminService.generateTicketFriendlyId(Concert.class);
    }
}