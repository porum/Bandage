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

fun Throwable.isChoreographerException(): Boolean {
  val stackTrace = this.stackTrace
  if (stackTrace.isNullOrEmpty()) {
    return false
  }

  var lastIndex = stackTrace.size - 1
  while (lastIndex >= 0 && stackTrace.size - lastIndex <= 20) {
    val element = stackTrace[lastIndex]
    if (element.className == "android.view.Choreographer" &&
      element.fileName == "Choreographer.java" &&
      element.methodName == "doFrame"
    ) {
      return true
    }
    lastIndex--
  }

  return false
}