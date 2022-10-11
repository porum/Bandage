package com.panda912.bandage.hook

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.panda912.bandage.logger.BandageLogger

/**
 * https://android.googlesource.com/platform/frameworks/base/+/525caa44ceda39cf5bc0823f1ef293865b5a5e30%5E%21/#F0
 *
 * Created by panda on 2022/10/10 17:03
 */
object ViewRootImplHandlerHooker : Application.ActivityLifecycleCallbacks {
  private const val TAG = "ViewRootImplHandlerHooker"

  fun hook(application: Application) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // == enough?
      application.registerActivityLifecycleCallbacks(this)
    }
  }

  override fun onActivityStarted(activity: Activity) {
    activity.window.decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
      override fun onViewAttachedToWindow(v: View) {
        try {
          val handler = v.handler
          if (handler != null) {
            val field = Handler::class.java.getDeclaredField("mCallback").apply { isAccessible = true }
            val callback = field.get(handler)
            if (callback == null || callback.javaClass.name != ProtectViewRootHandlerCallback::class.java.name) {
              field.set(handler, ProtectViewRootHandlerCallback(handler))
              BandageLogger.i(TAG, "ViewRootImpl handler hook successful.")
            }
          }
        } catch (th: Throwable) {
          BandageLogger.w(TAG, "ViewRootImpl handler hook failed.", th)
        }
      }

      override fun onViewDetachedFromWindow(v: View) {

      }
    })
  }

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

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

  }

  class ProtectViewRootHandlerCallback(private val handler: Handler) : Handler.Callback {

    override fun handleMessage(msg: Message): Boolean {
      try {
        handler.handleMessage(msg)
      } catch (th: Throwable) {
        BandageLogger.w(TAG, "handle message occur error, message: $msg", th)
      }
      return true
    }
  }
}