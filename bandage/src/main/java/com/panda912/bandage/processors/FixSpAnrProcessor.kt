package com.panda912.bandage.processors

import com.panda912.bandage.Processor
import com.panda912.bandage.sp.SpAnrHelper

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