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
