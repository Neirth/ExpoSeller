package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class ConcertsListViewModel extends ViewModel {
    private final AdminService mAdminService;
    private final ExecutorService mExecutorService;

    @Inject
    public ConcertsListViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    public ConcertRepo getConcertRepo() {
        return mAdminService.getConcertRepo();
    }

    public void searchListConcerts(AdminService.TypeSearch typeSearch, long parameter) {
        mExecutorService.submit(() -> {
            try {
                mAdminService.searchListModels(Concert.class, typeSearch, parameter);
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

    public LiveData<List<Concert>> getListConcerts() {
        return mAdminService.getListConcerts();
    }
}