package com.example.speechtotext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseActivity : AppCompatActivity() {
    private lateinit var btnAdv : Button
    private lateinit var btnMail : Button
    private lateinit var btnBg : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        btnAdv = findViewById(R.id.btnAdv)
        btnMail = findViewById(R.id.btnMail)
        btnBg = findViewById(R.id.btnBackground)

        btnAdv.setOnClickListener {
            intent = Intent(this@ChooseActivity, AdivinanzasActivity::class.java)
            startActivity(intent)
        }

        btnMail.setOnClickListener {
            intent = Intent(this@ChooseActivity, EmailActivity::class.java)
            startActivity(intent)
        }

        btnBg.setOnClickListener {
            intent = Intent(this@ChooseActivity, BackgroundActivity::class.java)
            startActivity(intent)
        }
    }
}