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
