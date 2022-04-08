package com.panda912.bandage.interceptors

/**
 * Created by panda on 2021/12/13 17:56
 */
interface IExceptionInterceptor {
  fun getName(): String
  fun shouldEnableOpt(): Boolean = true
  fun intercept(thread: Thread, throwable: Throwable): Boolean
}