package io.smartinez.exposeller.client.ui.adminconsole.fragment.concertslist;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.AdBanner;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.domain.IModel;
import io.smartinez.exposeller.client.repository.ConcertRepo;
import io.smartinez.exposeller.client.service.AdminService;

@HiltViewModel
public class ConcertsListViewModel extends ViewModel {
    private AdminService mAdminService;

    @Inject
    public ConcertsListViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    public ConcertRepo getConcertRepo() {
        return mAdminService.getConcertRepo();
    }

    public void searchListModels(AdminService.TypeSearch typeSearch, long parameter) throws IOException, IllegalAccessException {
        mAdminService.searchListModels(Concert.class, typeSearch, parameter);
    }

    public void notifyListChanges() throws IOException, IllegalAccessException {
        mAdminService.notifyListChanges();
    }

    public LiveData<List<Concert>> getListConcerts() {
        return mAdminService.getListConcerts();
    }
}