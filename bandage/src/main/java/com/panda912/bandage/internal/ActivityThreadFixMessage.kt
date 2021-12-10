package com.panda912.bandage.internal

/**
 * Created by panda on 2021/12/6 16:38
 */
data class ActivityThreadFixMessage(
  var msgId: Int,
  val msgName: String,
  val keywords: Set<String>? = null,
)