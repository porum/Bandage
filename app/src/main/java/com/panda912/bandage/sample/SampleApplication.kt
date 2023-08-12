package com.panda912.bandage.sample

import android.app.Application
import android.content.Context
import com.panda912.bandage.Bandage
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.sample.bandage.BandageConfig
import me.weishu.reflection.Reflection

/**
 * Created by panda on 2021/12/7 14:38
 */
class SampleApplication : Application() {

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    Reflection.unseal(base)
  }

  override fun onCreate() {
    super.onCreate()
    Bandage.install(BandageConfig(this))
    createDynamicBandageData()
  }

  private fun createDynamicBandageData() {
    // click button 'crash 10 / 0'
    val stack =
      "at com.panda912.bandage.sample.MainActivity.onCreate\$lambda-0(MainActivity.kt:14)\n" +
          "at com.panda912.bandage.sample.MainActivity.\$r8\$lambda\$DDlE6wDlIjCyOonj-NgrVOem29Q(Unknown Source:0)\n" +
          "at com.panda912.bandage.sample.MainActivity\$\$ExternalSyntheticLambda0.onClick(Unknown Source:0)\n" +
          "at android.view.View.performClick(View.java:7506)"
    val data = DynamicBandageData(
      process = "all",
      exceptionMatch = DynamicBandageData.ExceptionMatch("java.lang.ArithmeticException", ""),
      stacks = stack.lines().map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
      causes = null,
      closeCurActivity = false,
      loadPatch = false,
    )

    Bandage.addDynamicBandageData(listOf(data))
  }
}