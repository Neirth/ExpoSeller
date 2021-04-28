package io.smartinez.exposeller.client.ui.mainscreen.fragment.adminlogin;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.service.AdminService;

import java.io.IOException;

@HiltViewModel
public class AdminLoginViewModel extends ViewModel {
    private AdminService mAdminService;

    @Inject
    public AdminLoginViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    public void loginAdministrator(String email, String password) throws IOException {
        mAdminService.loginAdministrator(email, password);
    }

    public void logoutAdministrator() throws IOException {
        mAdminService.logoutAdministrator();
    }
}