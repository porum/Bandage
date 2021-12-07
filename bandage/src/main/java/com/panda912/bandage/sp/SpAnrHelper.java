package com.panda912.bandage.sp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by panda on 2021/12/7 10:56
 */
@SuppressLint("PrivateApi")
public class SpAnrHelper {
  private static final String TAG = "SpAnrHelper";

  private static boolean hasGetPendingWorkFinishers = false;
  private static ConcurrentLinkedQueue<Runnable> sPendingWorkFinishers = null;

  public static void fix(Message message) {
    if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
      int what = message.what;
      if (what == 103 || what == 104) {
        clearPendingWorkFinishers("STOP_ACTIVITY");
      } else if (what == 115) {
        clearPendingWorkFinishers("SERVICE_ARGS");
      } else if (what == 116) {
        clearPendingWorkFinishers("STOP_SERVICE");
      } else if (what == 137) {
        clearPendingWorkFinishers("SLEEPING");
      }
    }
  }

  private static void clearPendingWorkFinishers(String tag) {
    if (!hasGetPendingWorkFinishers) {
      getPendingWorkFinishers();
      hasGetPendingWorkFinishers = true;
    }
    Log.i(TAG, "fix sp anr " + tag);
    if (sPendingWorkFinishers != null) {
      Log.i(TAG, "clear PendingWorkFinishers ");
      sPendingWorkFinishers.clear();
    }
  }

  private static void getPendingWorkFinishers() {
    try {
      Field field = Class.forName("android.app.QueuedWork").getDeclaredField("sPendingWorkFinishers");
      if (field != null) {
        field.setAccessible(true);
        sPendingWorkFinishers = (ConcurrentLinkedQueue<Runnable>) field.get(null);
      }
    } catch (Throwable th) {
      Log.w(TAG, "hook fail.", th);
    }
  }

}
