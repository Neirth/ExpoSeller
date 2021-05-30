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
