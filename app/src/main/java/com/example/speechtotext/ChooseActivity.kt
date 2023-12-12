package com.example.speechtotext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseActivity : AppCompatActivity() { // crea la clase ChooseActivity
    // crea 3 variable de inicio tardio tipo boton
    private lateinit var btnAdv : Button
    private lateinit var btnMail : Button
    private lateinit var btnBg : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose) //establece el contenido de la actividad

        // asigna las referencias en cada variable
        btnAdv = findViewById(R.id.btnAdv)
        btnMail = findViewById(R.id.btnMail)
        btnBg = findViewById(R.id.btnBackground)

        // crea un onClicklistener el cual crea un intent hacia AdivinanzasActivity e inicia el intent.
        btnAdv.setOnClickListener {
            intent = Intent(this@ChooseActivity, AdivinanzasActivity::class.java)
            startActivity(intent)
        }

        // crea un onClicklistener el cual crea un intent hacia EmailActivity e inicia el intent.
        btnMail.setOnClickListener {
            intent = Intent(this@ChooseActivity, EmailActivity::class.java)
            startActivity(intent)
        }

        // crea un onClicklistener el cual crea un intent hacia BackgroundActivity e inicia el intent.
        btnBg.setOnClickListener {
            intent = Intent(this@ChooseActivity, BackgroundActivity::class.java)
            startActivity(intent)
        }
    }
}