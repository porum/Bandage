package com.panda912.bandage.hook.activity_thread_hook

import android.os.Message

/**
 * Created by panda on 2021/12/6 16:36
 */
interface Processor {
  fun process(chain: Chain): Boolean

  interface Chain {
    fun input(): Message
    fun proceed(input: Message): Boolean
  }
}