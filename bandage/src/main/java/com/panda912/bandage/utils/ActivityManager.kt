package com.panda912.bandage.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.SoftReference
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by panda on 2021/12/16 10:13
 */
class ActivityManager : Application.ActivityLifecycleCallbacks {

  private val activityList = CopyOnWriteArrayList<SoftReference<Activity>>()

  fun getCurActivity(): Activity? {
    if (activityList.isEmpty()) {
      return null
    }
    return activityList[activityList.size - 1]?.get()
  }

  fun isDestroyed(activity: Activity?): Boolean =
    activity == null || activity.isFinishing || activity.isDestroyed

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    activityList.add(SoftReference(activity))
  }

  override fun onActivityStarted(activity: Activity) {

  }

  override fun onActivityResumed(activity: Activity) {

  }

  override fun onActivityPaused(activity: Activity) {

  }

  override fun onActivityStopped(activity: Activity) {

  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

  }

  override fun onActivityDestroyed(activity: Activity) {
    for (softReference in activityList) {
      if (softReference.get() === activity) {
        activityList.remove(softReference)
        return
      }
    }
  }

  companion object {
    @Volatile
    private var instance: ActivityManager? = null

    fun getInstance(): ActivityManager =
      instance ?: synchronized(this) {
        instance ?: ActivityManager().also { instance = it }
      }

  }
}