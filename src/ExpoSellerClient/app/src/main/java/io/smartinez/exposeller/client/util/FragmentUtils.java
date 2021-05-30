package io.smartinez.exposeller.client.util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

/**
 * Class to manage the transitions between fragments
 */
public class FragmentUtils {
    /**
     * Method to interchange the fragment with other
     *
     * @param fragmentManager The fragment manager
     * @param containerSrc The container source
     * @param fragmentClassDest The fragment class to use
     */
    public static void interchangeFragement(FragmentManager fragmentManager, int containerSrc, Class<? extends Fragment> fragmentClassDest) {
        interchangeFragement(fragmentManager, containerSrc, fragmentClassDest, new Bundle());
    }

    /**
     * Method to interchange the fragment with other with bundle support
     *
     * @param fragmentManager The fragment manager
     * @param containerSrc The container source
     * @param fragmentClassDest The fragment class to use
     * @param args The bundle with options
     */
    public static void interchangeFragement(FragmentManager fragmentManager, int containerSrc, Class<? extends Fragment> fragmentClassDest, Bundle args) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(containerSrc, fragmentClassDest, args, UUID.randomUUID().toString()).commitNow();
    }
}
