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
package io.smartinez.exposeller.client.ui.checkschedules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.domain.Concert;
import io.smartinez.exposeller.client.service.UserService;

@HiltViewModel
public class CheckSchedulesViewModel extends ViewModel {
    // Instance for use case user
    private final UserService mUsersService;

    // Instance for executor service
    private final ExecutorService mExecutorService;

    @Inject
    public CheckSchedulesViewModel(UserService mUsersService, ExecutorService executorService) {
        this.mUsersService = mUsersService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to search in background the concerts with specific date
     *
     * @param date The date object
     * @return The live data list
     */
    public LiveData<List<Concert>> searchConcertWithDay(Date date) {
        // Instance a mutable live data
        MutableLiveData<List<Concert>> concertList = new MutableLiveData<>();

        // Run background the query
        mExecutorService.execute(() -> {
            try {
                concertList.postValue(mUsersService.searchConcertWithDay(date));
            } catch (IOException e) {
                concertList.postValue(Collections.emptyList());
            }
        });

        // Return the live data
        return concertList;
    }
}
