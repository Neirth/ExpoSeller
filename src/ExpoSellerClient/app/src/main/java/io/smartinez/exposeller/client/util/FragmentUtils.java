package io.smartinez.exposeller.client.util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public static void interchangeFragement(FragmentManager fragmentManager, int containerSrc, Class<? extends Fragment> fragmentClassDest) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(containerSrc, fragmentClassDest, new Bundle()).commitNow();
    }

    public static void interchangeFragement(FragmentManager fragmentManager, int containerSrc, Class<? extends Fragment> fragmentClassDest, Bundle args) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(containerSrc, fragmentClassDest, args).commitNow();
    }
}
