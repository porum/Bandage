package com.panda912.bandage.internal.hook.activity_thread_hook

import android.os.Message

/**
 * Created by panda on 2021/12/6 16:36
 */
internal interface Processor {
  fun process(chain: Chain): Boolean

  interface Chain {
    fun input(): Message
    fun proceed(input: Message): Boolean
  }
}