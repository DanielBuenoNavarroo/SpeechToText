package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
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

    // VARIABLES UTILIZADAS EN insertar_mail.xml, insertar_subject.xml y insertar_message.xml
    private lateinit var textViewTexto : TextView
    private lateinit var btnSpeak : Button
    private lateinit var btnSiguiente : Button

    // VARIABLES UTILIZADAS EN activity_mail.xml
    private lateinit var textTo: TextView
    private lateinit var textSubject: TextView
    private lateinit var editTextMessage: TextView
    private lateinit var btnSend: Button

    // VARIABLES DEL CORREO
    private lateinit var correo : String
    private lateinit var tema : String
    private lateinit var mensaje : String

    // MENSAJE
    private lateinit var mensajeEscuchado : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        iniciarInsertarMail()
    }

    private fun inicializarVistas() {
        textViewTexto = findViewById(R.id.textCorreo)
        btnSpeak = findViewById(R.id.buttonSpeakMail)
        btnSiguiente = findViewById(R.id.btnSiguiente)
        btnSiguiente.visibility = View.GONE
    }

    private fun actualizarVistas() {
        textViewTexto.text = mensajeEscuchado
        btnSiguiente.visibility = View.VISIBLE
    }

    private fun iniciarInsertarMail(){
        setContentView(R.layout.insertar_mail)
        inicializarVistas()

        btnSpeak.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }

        btnSiguiente.setOnClickListener {
            correo = textViewTexto.text.toString()
            iniciarInsertarSubject()
        }
    }

    private fun iniciarInsertarSubject(){
        setContentView(R.layout.insertar_subject)

        inicializarVistas()

        btnSpeak.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }

        btnSiguiente.setOnClickListener {
            tema = textViewTexto.text.toString()
            iniciarInsertarMessage()
        }
    }

    private fun iniciarInsertarMessage(){
        setContentView(R.layout.insertar_message)

        inicializarVistas()

        btnSpeak.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }

        btnSiguiente.setOnClickListener {
            mensaje = textViewTexto.text.toString()
            emailActivity()
        }
    }

    private fun emailActivity(){
        setContentView(R.layout.activity_email)

        Log.i("Mensaje", "empezado")
        textTo = findViewById(R.id.textEmailTo)
        textTo.text = correo
        Log.i("Mensaje", "textTo :)")
        textSubject = findViewById(R.id.textEmailSubject)
        textSubject.text = tema
        Log.i("Mensaje", "textSubject :)")
        editTextMessage = findViewById(R.id.textEmailMessage)
        editTextMessage.text = mensaje
        Log.i("Mensaje", "textMessage :)")
        btnSend = findViewById(R.id.emailButtonSend)
        Log.i("Mensaje", "terminado")
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

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak")

        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Kotlin")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toString()
                    mensajeEscuchado = result.substring(1, result.length - 1) // Se hace un substring porque viene en formato [texto] y le queremos quitar los corchetes
                    actualizarVistas()

                    // ESTO ES PARA VER SI EL MENSAJE SE RECOGE CORRECTAMENTE, ELIMINAR CUANDO ENTREGUEMOS
                    Log.i("Mensaje", mensajeEscuchado) // filtrar por Mensaje en el logcat para ver este log correctamente
                }
            }
        }
    }
}