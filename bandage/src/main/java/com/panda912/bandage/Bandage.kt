package com.panda912.bandage

import android.annotation.SuppressLint
import android.app.ActivityThread
import android.app.BandageActivityThread
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.SparseArray
import com.panda912.bandage.processors.*

/**
 * Created by panda on 2021/12/6 16:35
 */

interface ILogger {
  fun log(tag: String, message: String, throwable: Throwable?)

  companion object DEFAULT : ILogger {
    override fun log(tag: String, message: String, throwable: Throwable?) {
      Log.i(tag, message, throwable)
    }
  }
}

@SuppressLint("PrivateApi", "DiscouragedPrivateApi", "SoonBlockedPrivateApi")
object Bandage {
  private const val TAG = "Bandage"

  private const val TINKER_CALLBACK =
    "com.tencent.tinker.loader.TinkerResourceLoader\$ResourceStateMonitor\$HackerCallback"

  private lateinit var logger: ILogger

  private var mH: Handler? = null
  private var msgs = SparseArray<ActivityThreadFixMessage>()

  @JvmStatic
  fun log(tag: String = TAG, message: String, throwable: Throwable? = null) {
    logger.log(tag, message, throwable)
  }

  fun hook(logger: ILogger = ILogger.DEFAULT) {
    this.logger = logger
    if (hookActivityThreadHandler()) {
      prepareMessages()
    }
  }

  private fun hookActivityThreadHandler(): Boolean {
    if (mH != null) {
      return true
    }

    try {
      val activityThread = BandageActivityThread.currentActivityThread()
      val handler = with(ActivityThread::class.java.getDeclaredField("mH")) {
        isAccessible = true
        get(activityThread) as? Handler
      }

      val mCallbackField = Handler::class.java.getDeclaredField("mCallback")
      val callback = with(mCallbackField) {
        isAccessible = true
        get(handler) as? Handler.Callback
      }
      if (handler == null) {
        log(message = "hook ActivityThread\$mH fail, mH is null")
        return false
      } else if (callback == null || TINKER_CALLBACK == callback.javaClass.name) {
        mH = handler
        mCallbackField.set(handler, CallbackDelegate(callback))
        log(message = "hook ActivityThread\$mH success")
        return true
      } else {
        log(message = "hook ActivityThread\$mH failed, callback: " + callback.javaClass.name)
        return false
      }
    } catch (th: Throwable) {

    }

    return false
  }

  private fun prepareMessages() {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt >= Build.VERSION_CODES.P) {
      msgs.put(110, ActivityThreadFixMessage(110, "BIND_APPLICATION"))
      msgs.put(134, ActivityThreadFixMessage(134, "SCHEDULE_CRASH"))
      msgs.put(137, ActivityThreadFixMessage(137, "SLEEPING"))
      msgs.put(143, ActivityThreadFixMessage(143, "REQUEST_ASSIST_CONTEXT_EXTRAS"))
      if (sdkInt <= 30) {
        msgs.put(159, ActivityThreadFixMessage(159, "EXECUTE_TRANSACTION"))
      }
    } else {
      msgs.put(100, ActivityThreadFixMessage(100, "LAUNCH_ACTIVITY"))
      msgs.put(101, ActivityThreadFixMessage(101, "PAUSE_ACTIVITY"))
      msgs.put(103, ActivityThreadFixMessage(103, "STOP_ACTIVITY_SHOW"))
      msgs.put(104, ActivityThreadFixMessage(104, "STOP_ACTIVITY_HIDE"))
      msgs.put(107, ActivityThreadFixMessage(107, "RESUME_ACTIVITY"))
      msgs.put(109, ActivityThreadFixMessage(109, "DESTROY_ACTIVITY"))
      msgs.put(110, ActivityThreadFixMessage(110, "BIND_APPLICATION"))
      msgs.put(134, ActivityThreadFixMessage(134, "SCHEDULE_CRASH"))
      msgs.put(137, ActivityThreadFixMessage(137, "SLEEPING"))
      msgs.put(143, ActivityThreadFixMessage(143, "REQUEST_ASSIST_CONTEXT_EXTRAS"))
    }

    if (msgs.size() != 0) {
      checkMessages()
    }
  }

  private fun checkMessages() {
    val size = msgs.size()
    val sparseArray = SparseArray<ActivityThreadFixMessage>(size)
    for (i in 0 until size) {
      var message: ActivityThreadFixMessage? = null
      try {
        message = msgs.valueAt(i)
        val clazz = Class.forName("android.app.ActivityThread\$H")
        val code = clazz.getDeclaredField(message.msgName).get(clazz) as Int
        message.msgId = code
        sparseArray.put(code, message)
      } catch (th: Throwable) {
        log(message = "check $message fail", throwable = th)
      }
    }
    msgs = sparseArray
  }


  class CallbackDelegate(callback: Handler.Callback?) : Handler.Callback {

    private val processors = listOf(
      UiMsgInterceptor(),
      FixSpAnrProcessor(),
      FixMsg2StrCrashProcessor(),
      OriginCallbackProcessor(callback),
      FixActivityCrashProcessor(mH, msgs)
    )

    override fun handleMessage(msg: Message): Boolean {
      if (msg == null) {
        return false
      }
      val chain = MessageProcessorChain(processors, 0, msg)
      return chain.proceed(msg)
    }

  }
}