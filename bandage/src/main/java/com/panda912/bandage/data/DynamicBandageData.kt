package com.panda912.bandage.data

/**
 * Created by panda on 2021/12/13 18:03
 */
data class DynamicBandageData(
  val process: String,
  val className: String,
  val messageRegular: String? = null,
  val stacks: List<String>,
  val closeCurActivity: Boolean,
  val router: String? = null,
  val loadPatch: Boolean,
  val guideUpgrade: GuideUpgradeData? = null
) {
  data class GuideUpgradeData(
    val show: Boolean,
    val title: String,
    val tips: String,
    val desc: String,
    val okBtnText: String
  )
}