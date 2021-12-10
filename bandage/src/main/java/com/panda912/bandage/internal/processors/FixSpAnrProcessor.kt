package com.panda912.bandage.internal.processors

import com.panda912.bandage.internal.Processor
import com.panda912.bandage.internal.sp.SpAnrHelper

/**
 * Created by panda on 2021/12/6 17:00
 */
class FixSpAnrProcessor : Processor {

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()
    SpAnrHelper.fix(message)
    return chain.proceed(message)
  }
}