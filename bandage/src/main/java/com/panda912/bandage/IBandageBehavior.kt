package com.panda912.bandage

import com.panda912.bandage.internal.data.DynamicBandageData.GuideUpgradeData

/**
 * Created by panda on 2021/12/13 18:07
 */
interface IBandageBehavior {
  fun loadPatch()
  fun guideUpgrade(guideUpgradeData: GuideUpgradeData, closeCurActivity: Boolean)
  fun startRouter(router: String)
  fun finishCurActivity(throwable: Throwable)
  fun uploadCrash(throwable: Throwable)
}