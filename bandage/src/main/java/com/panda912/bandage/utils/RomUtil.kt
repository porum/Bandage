package com.panda912.bandage.utils

import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Method
import java.util.*

/**
 * Created by panda on 2021/1/6 10:13.
 */
object RomUtil {

  private var miuiVersion: String? = null

  private fun initMIUIVersion() {
    if (miuiVersion == null) {
      miuiVersion = getProp("ro.miui.ui.version.name") ?: ""
    }
  }

  @JvmStatic
  fun isMIUI(): Boolean {
    initMIUIVersion()
    return !miuiVersion.isNullOrEmpty() ||
        getProp("ro.miui.ui.version.code")?.isNotEmpty() == true ||
        getProp("ro.miui.internal.storage")?.isNotEmpty() == true
  }

  @JvmStatic
  fun isMIUI7(): Boolean {
    initMIUIVersion()
    return "V7" == miuiVersion
  }

  @JvmStatic
  fun isMIUI8(): Boolean {
    initMIUIVersion()
    return "V8" == miuiVersion
  }

  @JvmStatic
  fun isMIUI9(): Boolean {
    initMIUIVersion()
    return "V9" == miuiVersion
  }

  @JvmStatic
  fun isMIUI10(): Boolean {
    initMIUIVersion()
    return "V10" == miuiVersion
  }

  @JvmStatic
  fun isMIUI11(): Boolean {
    initMIUIVersion()
    return "V11" == miuiVersion
  }

  /**
   * miui12 空白通行证
   */
  @JvmStatic
  fun isMIUI12(): Boolean {
    initMIUIVersion()
    return "V12" == miuiVersion
  }

  @JvmStatic
  fun isHuaWei(): Boolean {
    var result = Build.MANUFACTURER?.contains("HUAWEI", true) ?: false
    if (!result) {
      result = !getProp("ro.build.version.emui").isNullOrEmpty()
    }
    return result
  }

  @JvmStatic
  fun isHonor(): Boolean {
    return Build.MANUFACTURER?.contains("HONOR", true) ?: false
  }

  @JvmStatic
  fun isHarmonyOS(): Boolean {
    return try {
      val clz = Class.forName("com.huawei.system.BuildEx")
      val method: Method = clz.getMethod("getOsBrand")
      "harmony" == method.invoke(clz)
    } catch (ex: Exception) {
      false
    }
  }

  @JvmStatic
  fun isOPPO(): Boolean {
    var result = Build.MANUFACTURER?.contains("OPPO", true) ?: false
    if (!result) {
      result = !getProp("ro.build.version.opporom").isNullOrEmpty()
    }
    return result
  }

  @JvmStatic
  fun isVIVO(): Boolean {
    var result = Build.MANUFACTURER?.contains("vivo", true) ?: false
    if (!result) {
      result = Build.MANUFACTURER?.contains("bbk", true) ?: false
    }
    if (!result) {
      result = !getProp("ro.vivo.os.version").isNullOrEmpty()
    }
    return result
  }

  @JvmStatic
  fun isMeizu(): Boolean {
    var result = Build.MANUFACTURER?.contains("meizu", true) ?: false
    if (!result) {
      result = Build.DISPLAY?.contains("FLYME", true) ?: false
    }
    return result
  }

  @JvmStatic
  fun isSamsung() = Build.MANUFACTURER?.contains("samsung", true) ?: Build.BRAND?.contains("samsung", true) ?: false

  @JvmStatic
  fun isSmartisan() = !getProp("ro.smartisan.version").isNullOrEmpty()
    || "smartisan".equals(Build.BRAND, true)
    || "smartisan".equals(Build.MANUFACTURER, true)

  @JvmStatic
  fun isLenovo() =
    !getProp("ro.lenovo.lvp.version").isNullOrEmpty()
      || "lenovo".equals(Build.BRAND, true)
      || "lenovo".equals(Build.MANUFACTURER, true)
      || "motorola".equals(Build.BRAND, true)
      || "motorola".equals(Build.MANUFACTURER, true)
      || "zuk".equals(Build.BRAND, true)
      || "zuk".equals(Build.MANUFACTURER, true)

  @JvmStatic
  fun isGionee() = !getProp("ro.gn.sv.version").isNullOrEmpty()
    || (!TextUtils.isEmpty(Build.DISPLAY) && Build.DISPLAY.lowercase(Locale.getDefault()).contains("amigo"))

  @JvmStatic
  fun isZTE() = Build.MANUFACTURER?.contains("ZTE", true) ?: false

  @JvmStatic
  fun isNubia() = Build.MANUFACTURER?.contains("NUBIA", true) ?: false

  @JvmStatic
  fun isQIKU() = Build.MANUFACTURER?.let { it.contains("QIKU", true) || it.contains("360") } ?: false

  @JvmStatic
  fun isLG(): Boolean {
    return ("lge".equals(Build.BRAND, true) || "lge".equals(Build.MANUFACTURER, true))
      && Build.MODEL != null && Build.MODEL.lowercase(Locale.getDefault()).contains("lg")
  }

  @JvmStatic
  fun isOnePlus() = Build.MANUFACTURER?.contains("oneplus", true) ?: false

  @JvmStatic
  fun isLetv() = !getProp("ro.letv.release.version").isNullOrEmpty()

  @JvmStatic
  fun isHi() = Build.HARDWARE?.lowercase(Locale.getDefault())?.contains("hi") ?: false

  private fun getProp(key: String): String? {
    try {
      BufferedReader(InputStreamReader(Runtime.getRuntime().exec("getprop $key").inputStream), 1024).use { br -> return br.readLine() }
    } catch (ignored: Throwable) {
      return null
    }
  }
}