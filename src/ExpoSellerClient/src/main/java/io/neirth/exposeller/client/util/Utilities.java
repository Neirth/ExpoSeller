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
package io.neirth.exposeller.client.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import io.neirth.exposeller.client.R;

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
     * @param view The view to hide the keyboard
     * @param context The activity context
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

    /**
     * Method to hide the system ui
     *
     * @param window The window object
     */
    public static void hideSystemUi(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
