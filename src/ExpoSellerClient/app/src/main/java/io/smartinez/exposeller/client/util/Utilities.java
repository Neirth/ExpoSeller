package io.smartinez.exposeller.client.util;

import android.app.Application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Random;

public class Utilities {
    public static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
    }

    public static Integer generateRandomFriendlyId() {
        int m = (int) Math.pow(10, 8);
        return m + (new Random()).nextInt(9 * m);
    }
}
