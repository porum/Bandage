package com.panda912.bandage.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btn).setOnClickListener {
      startActivity(Intent(this, DetailActivity::class.java))
    }
  }
}