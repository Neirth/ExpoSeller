/*
 * Copyright 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.neirth.exposeller.client.ui.adminconsole.fragment.adslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.neirth.exposeller.client.domain.Advertisement;
import io.neirth.exposeller.client.repository.AdvertisementRepo;
import io.neirth.exposeller.client.service.AdminService;

@HiltViewModel
public class AdsListViewModel extends ViewModel {
    // Instance of use cases layer
    private final AdminService mAdminService;

    // Instance of executor service
    private final ExecutorService mExecutorService;

    @Inject
    public AdsListViewModel(AdminService adminService, ExecutorService executorService) {
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
     * Method to get a live data with list of entities
     *
     * @return LiveData with List of Entities
     */
    public LiveData<List<Advertisement>> getListAdBanner() {
        return mAdminService.getAdvertisementList();
    }

    /**
     * Method to search with parameters in the repo
     *
     * @param typeSearch The type of search
     * @param parameter The parameter to search
     */
    public void searchListAdBanners(AdminService.TypeSearch typeSearch, long parameter) {
        mExecutorService.submit(() -> {
            try {
                mAdminService.searchListModels(Advertisement.class, typeSearch, parameter);
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