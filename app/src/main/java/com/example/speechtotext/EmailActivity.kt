package com.example.speechtotext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import javax.security.auth.Subject

class EmailActivity : AppCompatActivity() {
    private lateinit var textTo : TextView
    private lateinit var textSubject : TextView
    private lateinit var editTextMessage : TextView
    private lateinit var btnSend : Button
    private lateinit var msg : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        textTo = findViewById(R.id.textEmailTo)
        textSubject = findViewById(R.id.textEmailSubject)
        editTextMessage = findViewById(R.id.textEmailMessage)

        msg = intent.getStringExtra("MENSAJE").toString()
        editTextMessage.text = msg

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
        intent.putExtra(Intent.EXTRA_EMAIL, mail)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Elige una aplicación para enviar el mensaje"))
    }
}