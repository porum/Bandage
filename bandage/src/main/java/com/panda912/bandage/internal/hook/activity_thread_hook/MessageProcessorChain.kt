package com.panda912.bandage.internal.hook.activity_thread_hook

import android.os.Message

/**
 * Created by panda on 2021/12/6 16:50
 */
internal class MessageProcessorChain(
  private val processors: List<Processor>,
  private val index: Int,
  private val input: Message,
) : Processor.Chain {

  override fun input(): Message = input

  override fun proceed(input: Message): Boolean {
    check(index < processors.size)
    val processor = processors[index]
    val next = MessageProcessorChain(processors, index + 1, input)
    return processor.process(next)
  }
}