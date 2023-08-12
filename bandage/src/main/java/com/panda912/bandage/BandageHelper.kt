package com.panda912.bandage

import com.panda912.bandage.internal.data.DynamicBandageData.GuideUpgradeData

/**
 * Created by panda on 2021/12/13 18:19
 */
object BandageHelper {

  fun startRouter(router: String) {
    Bandage.config.behavior.startRouter(router)
  }

  fun uploadCrash(throwable: Throwable) {
    Bandage.config.behavior.uploadCrash(throwable)
  }

  fun loadPatch() {
    Bandage.config.behavior.loadPatch()
  }

  fun guideUpgrade(guideUpgradeData: GuideUpgradeData, closeCurActivity: Boolean) {
    Bandage.config.behavior.guideUpgrade(guideUpgradeData, closeCurActivity)
  }

  fun finishFatalActivity(throwable: Throwable) {
    Bandage.config.behavior.finishCurActivity(throwable)
  }

}