package com.panda912.bandage.data

import java.util.regex.Pattern

/**
 * Created by panda on 2021/12/13 18:03
 */
data class DynamicBandageData(
  val process: String,
  val exceptionMatch: ExceptionMatch?,
  val stacks: List<String>,
  val causes: List<ExceptionMatch>?,
  val closeCurActivity: Boolean,
  val router: String? = null,
  val loadPatch: Boolean,
  val guideUpgrade: GuideUpgradeData? = null
) {

  data class ExceptionMatch(
    val className: String,
    val messageRegular: String? = null
  ) {

    fun isMatch(th: Throwable): Boolean {
      if (th.javaClass.name != className) {
        return false
      }
      if (messageRegular.isNullOrEmpty()) {
        return true
      }
      val message = th.message
      if (message.isNullOrEmpty()) {
        return false
      }
      if (messageRegular == th.message || Pattern.matches(messageRegular, message)) {
        return true
      }
      return false
    }
  }

  data class GuideUpgradeData(
    val show: Boolean,
    val title: String,
    val tips: String,
    val desc: String,
    val okBtnText: String
  )
}

fun removeMatchedCause(list: ArrayList<DynamicBandageData.ExceptionMatch>, th: Throwable) {
  for (exceptionMatch in list) {
    if (exceptionMatch.isMatch(th)) {
      list.remove(exceptionMatch)
      return
    }
  }
}