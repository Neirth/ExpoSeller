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
package io.neirth.exposeller.client.ui.mainscreen.fragment.adminlogin;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.neirth.exposeller.client.service.AdminService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@HiltViewModel
public class AdminLoginViewModel extends ViewModel {
    // Instance for use case admin
    private AdminService mAdminService;

    // Instance for executor service
    private ExecutorService mExecutorService;

    @Inject
    public AdminLoginViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    /**
     * Method to call administrator login
     *
     * @param email Email of administrator
     * @param password Password of administrator
     * @return Return a future object
     */
    public CompletableFuture<Void> loginAdministrator(String email, String password) {
        // Prepare a future operation instance
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        // Run in background the operation
        mExecutorService.submit(() -> {
            try {
                // Run the login administration operation
                mAdminService.loginAdministrator(email, password);

                // Return the future operation
                completableFuture.complete(null);
            } catch (IOException e) {
                // Return the future operation exceptionally
                completableFuture.completeExceptionally(e);
            }
        });

        // Return the future operation object
        return completableFuture;
    }
}