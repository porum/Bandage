package com.panda912.bandage.interceptors

/**
 * Created by panda on 2021/12/13 17:56
 */
fun interface IExceptionInterceptor {
  fun intercept(thread: Thread, throwable: Throwable): Boolean
}