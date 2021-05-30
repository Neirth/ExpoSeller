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
import java.util.function.Consumer;

@HiltViewModel
public class AdminConsoleViewModel extends ViewModel {
    // Instance of use cases layer
    private final AdminService mAdminService;

    @Inject
    public AdminConsoleViewModel(AdminService adminService) {
        this.mAdminService = adminService;
    }

    /**
     * Method to open the system settings
     * 
     * @param activity Activity to get the operations
     */
    public void openSystemSettings(Activity activity) {
        try {
            // Try to open the Android Things Dashboard
            activity.startActivity(Intent.makeMainActivity(new ComponentName("com.android.iotlauncher", "com.android.iotlauncher.DefaultIoTLauncher")));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (ActivityNotFoundException e) {
            // If don't exist, open the default action settings
            activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    /**
     * Method for execute the exit operations from admin console
     *
     * @param context The activity context
     */
    public void exitAdminConsole(Context context, Runnable runnable) {
        try {
            // Logout from database admin account
            mAdminService.logoutAdministrator();

            // Run the rest of operations
            runnable.run();
        } catch (IOException e) {
            // Show the message error
            Toast.makeText(context, R.string.admin_logout_exception, Toast.LENGTH_LONG).show();
        }
    }
}
