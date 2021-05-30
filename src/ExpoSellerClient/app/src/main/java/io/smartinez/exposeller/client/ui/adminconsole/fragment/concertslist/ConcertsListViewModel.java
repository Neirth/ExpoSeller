/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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