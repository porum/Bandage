package com.panda912.bandage.utils

fun Throwable?.isOutOfMemoryError(): Boolean {
  if (this == null) return false
  var throwable: Throwable? = this
  var i = 0
  while (throwable != null && i < 20) {
    if (throwable is OutOfMemoryError) return true
    throwable = throwable.cause
    i++
  }
  return false
}