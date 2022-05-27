package com.bangkitacademy.foodergyapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mButton = findViewById<View>(R.id.button) as Button
        //handle onClick
        mButton.setOnClickListener {
            //intent to start NewActivity
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }
    }
}