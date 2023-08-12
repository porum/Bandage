package com.panda912.bandage.internal.hook.activity_thread_hook.processors

import com.panda912.bandage.internal.hook.activity_thread_hook.Processor
import com.panda912.bandage.internal.hook.activity_thread_hook.sp.SpAnrHelper

/**
 * Created by panda on 2021/12/6 17:00
 */
internal class FixSpAnrProcessor : Processor {

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()
    SpAnrHelper.fix(message)
    return chain.proceed(message)
  }
}