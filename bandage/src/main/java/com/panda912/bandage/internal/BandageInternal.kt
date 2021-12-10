package com.panda912.bandage.internal

import android.annotation.SuppressLint
import android.app.ActivityThread
import android.os.Build
import android.os.Handler
import android.util.SparseArray
import com.panda912.bandage.ILogger
import com.panda912.bandage.internal.processors.*

/**
 * Created by panda on 2021/12/6 16:35
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi", "SoonBlockedPrivateApi")
class BandageInternal {
  companion object {
    private const val TAG = "Bandage"
  }

  private var uninstallActivityThreadHandlerCallback: (() -> Unit)? = null

  fun install(logger: ILogger) {
    check(uninstallActivityThreadHandlerCallback == null) {
      "Bandage already installed"
    }
    BandageLogger.logger = logger
    try {
      swapActivityThreadHandlerCallback { mH, mCallback ->
        uninstallActivityThreadHandlerCallback = {
          swapActivityThreadHandlerCallback { _, _ ->
            mCallback
          }
        }
        val processors = listOf(
          UiMsgInterceptor(),
          FixSpAnrProcessor(),
          FixMsg2StrCrashProcessor(),
          OriginCallbackProcessor(mCallback),
          FixActivityCrashProcessor(mH, getFixMessages())
        )
        Handler.Callback { msg ->
          if (msg == null) {
            return@Callback false
          }
          val chain = MessageProcessorChain(processors, 0, msg)
          return@Callback chain.proceed(msg)
        }
      }
    } catch (ignored: Throwable) {
    }
  }

  private fun swapActivityThreadHandlerCallback(swap: (Handler, Handler.Callback?) -> Handler.Callback?) {
    val activityThread = ActivityThread.currentActivityThread()
    val mHField = ActivityThread::class.java.getDeclaredField("mH").apply { isAccessible = true }
    val mH = mHField[activityThread] as Handler

    val mCallbackField =
      Handler::class.java.getDeclaredField("mCallback").apply { isAccessible = true }
    val mCallback = mCallbackField[mH] as? Handler.Callback
    mCallbackField[mH] = swap(mH, mCallback)
  }

  private fun getFixMessages(): SparseArray<ActivityThreadFixMessage>? {
    val msgs = SparseArray<ActivityThreadFixMessage>()
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

    return if (msgs.size() != 0) filterValidMessages(msgs) else null
  }

  private fun filterValidMessages(msgs: SparseArray<ActivityThreadFixMessage>): SparseArray<ActivityThreadFixMessage> {
    val size = msgs.size()
    val validMsgs = SparseArray<ActivityThreadFixMessage>(size)
    for (i in 0 until size) {
      var message: ActivityThreadFixMessage? = null
      try {
        message = msgs.valueAt(i)
        val clazz = Class.forName("android.app.ActivityThread\$H")
        val code = clazz.getDeclaredField(message.msgName).get(clazz) as Int
        message.msgId = code
        validMsgs.put(code, message)
      } catch (th: Throwable) {
        BandageLogger.w(TAG, "check $message fail", th)
      }
    }
    return validMsgs
  }

}