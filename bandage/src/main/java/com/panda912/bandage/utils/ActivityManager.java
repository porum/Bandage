package com.panda912.bandage.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by panda on 2020/12/10 15:29
 */
public class ActivityManager implements Application.ActivityLifecycleCallbacks {
  private static volatile ActivityManager sInstance;

  private final List<SoftReference<Activity>> mActivityList = new CopyOnWriteArrayList<>();

  private ActivityManager() {
  }

  public static ActivityManager getInstance() {
    if (sInstance == null) {
      synchronized (ActivityManager.class) {
        if (sInstance == null) {
          sInstance = new ActivityManager();
        }
      }
    }
    return sInstance;
  }

  @Nullable
  public Activity getCurActivity() {
    if (mActivityList.isEmpty()) {
      return null;
    }

    SoftReference<Activity> softReference = mActivityList.get(mActivityList.size() - 1);
    if (softReference == null) {
      return null;
    }
    return softReference.get();
  }

  public boolean isDestroyed(Activity activity) {
    return activity == null || activity.isFinishing() || activity.isDestroyed();
  }

  @Override
  public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    mActivityList.add(new SoftReference<>(activity));
  }

  @Override
  public void onActivityStarted(@NonNull Activity activity) {

  }

  @Override
  public void onActivityResumed(@NonNull Activity activity) {

  }

  @Override
  public void onActivityPaused(@NonNull Activity activity) {

  }

  @Override
  public void onActivityStopped(@NonNull Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

  }

  @Override
  public void onActivityDestroyed(@NonNull Activity activity) {
    for (SoftReference<Activity> softReference : mActivityList) {
      if (softReference.get() == activity) {
        mActivityList.remove(softReference);
        return;
      }
    }
  }
}
