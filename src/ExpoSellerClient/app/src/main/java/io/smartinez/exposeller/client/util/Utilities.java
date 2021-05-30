package io.smartinez.exposeller.client.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import io.smartinez.exposeller.client.R;

import java.util.Random;

public class Utilities {
    /**
     * Method to get the application instance in background
     *
     * @return The application object
     * @throws Exception Any exception
     */
    @SuppressLint("PrivateApi")
    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
    }

    /**
     * Method to generate a random friendly id
     *
     * @return The friendly id without checks
     */
    public static Integer generateRandomFriendlyId() {
        // Raise the number to eight
        int m = (int) Math.pow(10, 8);

        // Generate a random number
        return m + (new Random()).nextInt(9 * m);
    }

    /**
     * Method to hidden the keyboard
     *
     * @param context The activity context
     * @param view The view to hide the keyboard
     */
    public static void hideKeyboard(Context context, View view) {
        // Get a system service
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // If not null, hides the keyboard
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Method to show a custom toast
     *
     * @param context The activity context
     * @param stringResource The string resource to show
     */
    public static void showToast(Context context, int stringResource) {
        // Get a new instance of toast
        Toast toast = Toast.makeText(context, stringResource, Toast.LENGTH_SHORT);

        // Custom the value of background
        toast.getView().setBackgroundColor(context.getResources().getColor(R.color.darken_grey_transparent));

        // Get a text view of toast
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);

        // Custom the value of foreground
        toastMessage.setTextColor(Color.WHITE);

        // Show the toast
        toast.show();
    }
}
