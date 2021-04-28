package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertsmgt;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class ConcertsMgtViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private AdminService mAdminService;

    @Inject
    public ConcertsMgtViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    public ConcertRepo getConcertRepo() {
        return mAdminService.getConcertRepo();
    }
}