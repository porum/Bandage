package com.panda912.bandage.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import kotlin.concurrent.thread

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btn).setOnClickListener {
      startActivity(Intent(this, DetailActivity::class.java))
    }

    findViewById<Button>(R.id.btn2).setOnClickListener {
      throw WindowManager.BadTokenException("token is not valid; is your activity running?")
    }

    findViewById<Button>(R.id.btn3).setOnClickListener {
      println(10 / 0)
    }

    findViewById<Button>(R.id.btn4).setOnClickListener {
      thread {
        throw RuntimeException("crash in sub thread.")
      }
    }
  }
}