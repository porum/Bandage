package com.panda912.bandage.sample

import android.app.Activity
import android.os.Bundle

class DetailActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    println(10 / 0)
  }
}