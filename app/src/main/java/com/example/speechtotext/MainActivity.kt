package com.example.speechtotext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnSiguiente : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSiguiente = findViewById(R.id.darAdivinanza)
        btnSiguiente.setOnClickListener {
            intent = Intent(this@MainActivity, ChooseActivity::class.java)
            startActivity(intent)
        }
    }

}