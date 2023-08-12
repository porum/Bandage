package com.panda912.bandage.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<Button>(R.id.button).setOnClickListener {
      println(10 / 0)
    }
  }
}