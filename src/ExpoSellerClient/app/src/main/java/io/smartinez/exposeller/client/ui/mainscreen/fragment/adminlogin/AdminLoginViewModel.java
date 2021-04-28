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
    private AdminService mAdminService;
    private ExecutorService mExecutorService;

    @Inject
    public AdminLoginViewModel(AdminService adminService, ExecutorService executorService) {
        this.mAdminService = adminService;
        this.mExecutorService = executorService;
    }

    public CompletableFuture<Void> loginAdministrator(String email, String password) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        mExecutorService.submit(() -> {
            try {
                mAdminService.loginAdministrator(email, password);
                completableFuture.complete(null);
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    public CompletableFuture<Void> logoutAdministrator() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        mExecutorService.submit(() -> {
            try {
                mAdminService.logoutAdministrator();
                completableFuture.complete(null);
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}