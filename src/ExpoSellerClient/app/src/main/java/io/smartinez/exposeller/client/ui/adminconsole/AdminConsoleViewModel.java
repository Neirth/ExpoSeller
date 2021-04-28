package io.smartinez.exposeller.client.ui.adminconsole;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.smartinez.exposeller.client.R;
import io.smartinez.exposeller.client.service.AdminService;

import javax.inject.Inject;
import java.io.IOException;

@HiltViewModel
public class AdminConsoleViewModel extends ViewModel {
    private AdminService mAdminService;

    @Inject
    public AdminConsoleViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    public void openSystemSettings(Activity activity) {
        try {
            activity.startActivity(Intent.makeMainActivity(new ComponentName("com.android.iotlauncher", "com.android.iotlauncher.DefaultIoTLauncher")));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void exitAdminConsole(Activity activity) {
        try {
            mAdminService.logoutAdministrator();
            activity.onBackPressed();
        } catch (IOException e) {
            Toast.makeText(activity, R.string.admin_logout_exception, Toast.LENGTH_LONG).show();
        }
    }
}
