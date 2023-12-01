package com.example.speechtotext

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmailActivity : AppCompatActivity() {
    private lateinit var textTo: TextView
    private lateinit var textSubject: TextView
    private lateinit var editTextMessage: TextView
    private lateinit var btnSend: Button
    private var cont = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarInsertarMail()
    }

    private fun iniciarInsertarMail(){
        setContentView(R.layout.insertar_mail)

        val btnSiguiente : Button = findViewById(R.id.btnSiguiente)
        btnSiguiente.setOnClickListener {
            iniciarInsertarSubject()
        }
    }

    private fun iniciarInsertarSubject(){
        setContentView(R.layout.insertar_subject)

        val btnSiguiente : Button = findViewById(R.id.btnSiguiente)
        btnSiguiente.setOnClickListener {
            iniciarInsertarMessage()
        }
    }

    private fun iniciarInsertarMessage(){
        setContentView(R.layout.insertar_message)

        val btnSiguiente : Button = findViewById(R.id.btnSiguiente)
        btnSiguiente.setOnClickListener {
            emailActivity()
        }

    }

    private fun emailActivity(){
        setContentView(R.layout.activity_email)

        textTo = findViewById(R.id.textEmailTo)
        textSubject = findViewById(R.id.textEmailSubject)
        editTextMessage = findViewById(R.id.textEmailMessage)
        editTextMessage.text = intent.getStringExtra("MENSAJE")
        btnSend = findViewById(R.id.emailButtonSend)

        btnSend.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        val mail = editTextMessage.text.toString()
        val subject = textSubject.text.toString()
        val message = editTextMessage.text.toString()

        intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Elige una aplicación para enviar el mensaje"))
    }
}
