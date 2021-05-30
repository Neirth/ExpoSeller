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

import android.os.Handler;

/**
 * Class to manage the time idle for the user
 */
public class TimeOutIdle {
    // Instance of handler
    private static Handler mHandler;

    // Instance of idle runnable
    private static Runnable mIdleRunnable;

    /**
     * Method to init the idle handle
     *
     * @param runnable The callback when the time idle is finish
     */
    public synchronized static void initIdleHandler(Runnable runnable) {
        if (mHandler == null && mIdleRunnable == null) {
            // Instance a new handle
            mHandler = new Handler();
        } else {
            // Stop the previous handler
            TimeOutIdle.stopIdleHandler();
        }

        // Associate a new callback
        mIdleRunnable = runnable;
    }

    /**
     * Method to stop the idle handler
     */
    public synchronized static void stopIdleHandler() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Method to start the idle handler
     */
    public synchronized static void startIdleHandler() {
        mHandler.postDelayed(mIdleRunnable, 60 * 1000);
    }

    /**
     * Method to start the idle handler
     *
     * @param milliseconds The milliseconds to finish the idle
     */
    public synchronized static void startIdleHandler(long milliseconds) {
        mHandler.postDelayed(mIdleRunnable, milliseconds);
    }

    /**
     * Method to reset the time idle
     */
    public static void resetIdleHandle() {
        TimeOutIdle.resetIdleHandle(60 * 1000);
    }

    /**
     * Method to reset the time idle
     *
     * @param milliseconds The milliseconds to finish the idle
     */
    public static void resetIdleHandle(long milliseconds) {
        stopIdleHandler();
        startIdleHandler(milliseconds);
    }
}
