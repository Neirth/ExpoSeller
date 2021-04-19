package io.smartinez.exposeller.client.util;

import android.os.Handler;

public class TimeOutIdle {
    private static Handler mHandler;
    private static Runnable mIdleRunnable;

    public synchronized static void initIdleHandler(Runnable runnable) {
        if (mHandler == null && mIdleRunnable == null) {
            mHandler = new Handler();
        } else {
            TimeOutIdle.stopIdleHandler();
        }

        mIdleRunnable = runnable;
    }

    public synchronized static void stopIdleHandler() {
        mHandler.removeCallbacks(mIdleRunnable);
    }

    public synchronized static void startIdleHandler() {
        mHandler.postDelayed(mIdleRunnable, 60 * 1000);
    }
}
