package com.panda912.bandage

import com.panda912.bandage.data.DynamicBandageData

/**
 * Created by panda on 2021/12/15 17:30
 */
object BandageDynamicExceptionManager {

  private val dynamicBandageDataList = arrayListOf<DynamicBandageData>()

  fun addData(list: List<DynamicBandageData>?) {
    synchronized(this) {
      dynamicBandageDataList.clear()
      if (!list.isNullOrEmpty()) {
        dynamicBandageDataList.addAll(list.filter { BandageDataMatcher.isProcessMatch(it) })
      }
    }
  }

  @Synchronized
  fun getDynamicBandageData(th: Throwable): DynamicBandageData? {
    if (dynamicBandageDataList.isEmpty()) {
      return null
    }

    for (data in dynamicBandageDataList) {
      if (BandageDataMatcher.isProcessMatch(data) &&    // process √
        data.exceptionMatch?.isMatch(th) == true &&     // class name && message √
        BandageDataMatcher.isStackMatch(data, th)       // stack √
      ) {
        return data
      }
    }
    return null
  }

}