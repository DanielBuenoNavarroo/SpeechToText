package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class EmailActivity : AppCompatActivity() {
    private val REQ_CODE = 100

    private lateinit var textViewCorreo: TextView
    private lateinit var textViewSubject: TextView
    private lateinit var textViewMessage: TextView
    private lateinit var btnSend: Button

    private lateinit var mensajeEscuchado: String
    private lateinit var pedir: String
    private val pedirSubject = "Cual quieres que sea el tema?"
    private val pedirMsg = "Cual quieres que sea el mensaje a entregar?"

    private val correo = "ejemplo@gmail.com"
    private var tema = ""
    private var mensaje = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        textViewCorreo = findViewById(R.id.TextViewCorreo)
        textViewCorreo.text = correo
        textViewSubject = findViewById(R.id.TextViewSubject)
        textViewMessage = findViewById(R.id.TextViewMessage)
        btnSend = findViewById(R.id.button)

        pedirSubject()
    }

    private fun pedirSubject() {
        Log.i("mensaje", "sub")
        pedir = pedirSubject
        iniciarReconocimientoDeVoz()
    }

    private fun pedirMessage() {
        Log.i("mensaje", "msgg")
        pedir = pedirMsg
        iniciarReconocimientoDeVoz()
    }

    private fun mailActivity() {
        Log.i("mensaje", "email")
        textViewSubject.text = tema
        textViewMessage.text = mensaje
        btnSend.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:") // Usamos ACTION_SENDTO y establecemos el esquema como "mailto:"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
        intent.putExtra(Intent.EXTRA_SUBJECT, tema)
        intent.putExtra(Intent.EXTRA_TEXT, mensaje)
        intent.type = "message/rfc822"

        startActivity(Intent.createChooser(intent, "Elige una aplicación para enviar el mensaje"))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No hay aplicaciones de correo disponibles", Toast.LENGTH_SHORT).show()
        }
    }

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, pedir)

        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                mensajeEscuchado = results[0]
                Log.i("mensaje", mensajeEscuchado)

                if (pedir == pedirSubject) {
                    tema = mensajeEscuchado
                    pedirMessage()
                } else if (pedir == pedirMsg) {
                    mensaje = mensajeEscuchado
                    mailActivity()
                }
            }
        }
    }
}