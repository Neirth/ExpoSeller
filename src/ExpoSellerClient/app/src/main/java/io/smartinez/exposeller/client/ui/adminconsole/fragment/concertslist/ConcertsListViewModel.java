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
    // Instance of use cases layer
    private final AdminService mAdminService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    @Inject
    public ConcertsListViewModel(AdminService adminService, ExecutorService executorService) {
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
     * Method to get a live data with list of entities
     *
     * @return LiveData with List of Entities
     */
    public LiveData<List<Concert>> getListConcerts() {
        return mAdminService.getConcertList();
    }

    /**
     * Method to search with parameters in the repo
     *
     * @param typeSearch The type of search
     * @param parameter The parameter to search
     */
    public void searchListConcerts(AdminService.TypeSearch typeSearch, long parameter) {
        mExecutorService.submit(() -> {
            try {
                mAdminService.searchListModels(Concert.class, typeSearch, parameter);
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