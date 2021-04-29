package io.smartinez.exposeller.client.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import io.smartinez.exposeller.client.R;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Utilities {
    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
    }

    public static Integer generateRandomFriendlyId() {
        int m = (int) Math.pow(10, 8);
        return m + (new Random()).nextInt(9 * m);
    }

    public static void hideKeyboard(@NotNull Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showToast(Activity activity, int stringResource) {
        Toast toast = Toast.makeText(activity, stringResource, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(activity.getResources().getColor(R.color.darken_grey_transparent));

        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);

        toast.show();
    }
}
