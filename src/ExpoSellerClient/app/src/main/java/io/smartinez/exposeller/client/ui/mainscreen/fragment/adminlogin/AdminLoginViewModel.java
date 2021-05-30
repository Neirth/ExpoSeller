package io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.AdminService;

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