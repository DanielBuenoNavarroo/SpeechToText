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

    // VARIABLES UTILIZADAS EN insertar_mail.xml, insertar_subject.xml y insertar_message.xml
    private lateinit var textViewTexto : TextView

    // VARIABLES UTILIZADAS EN activity_mail.xml
    private lateinit var textTo: TextView
    private lateinit var textSubject: TextView
    private lateinit var editTextMessage: TextView
    private lateinit var btnSend: Button

    // VARIABLES DEL CORREO
    private val correo = "ejemplo@gmail.com"
    private lateinit var tema : String
    private lateinit var mensaje : String

    // MENSAJE ESCUCHADO
    private lateinit var mensajeEscuchado : String
    private lateinit var mensajeComprobado : String

    // CONFIRMACION
    private var irAComprobacion = false
    private var elMensajeHaSidoComprobado = false

    // PANTALLA ACTUAL
    private lateinit var pantallaActual : Pantallas
    enum class Pantallas {Mail, Subject, Message, Principal}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //iniciarInsertarMail() // Sirve para iniciar la pantalla de meter email. como ahora esta harcodeado no se muestra.
        iniciarInsertarSubject()
    }

    private fun inicializarVistas() {
        textViewTexto = findViewById(R.id.textCorreo)
        mensajeComprobado = ""
        mensajeEscuchado = ""
        elMensajeHaSidoComprobado = false
        irAComprobacion = false
    }

    /*
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
    */

    private fun iniciarInsertarSubject(){
        setContentView(R.layout.insertar_subject)
        pantallaActual = Pantallas.Subject
        inicializarVistas()

        do {
            iniciarReconocimientoDeVoz()
        }while (!elMensajeHaSidoComprobado)

        tema = textViewTexto.text.toString()
        iniciarInsertarMessage()
    }

    private fun iniciarInsertarMessage(){
        setContentView(R.layout.insertar_message)
        pantallaActual = Pantallas.Message
        inicializarVistas()

        do {
            iniciarReconocimientoDeVoz()
        }while (!elMensajeHaSidoComprobado)

        mensaje = textViewTexto.text.toString()
        emailActivity()
    }

    private fun emailActivity(){
        setContentView(R.layout.activity_email)
        pantallaActual = Pantallas.Principal

        textTo = findViewById(R.id.textEmailTo)
        textTo.text = correo
        textSubject = findViewById(R.id.textEmailSubject)
        textSubject.text = tema
        editTextMessage = findViewById(R.id.textEmailMessage)
        editTextMessage.text = mensaje
        btnSend = findViewById(R.id.emailButtonSend)
        btnSend.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        val mail = arrayOf(correo)
        val subject = textSubject.text.toString()
        val message = editTextMessage.text.toString()

        intent.data = Uri.parse("mailto:") // Usamos ACTION_SENDTO y establecemos el esquema como "mailto:"
        intent.putExtra(Intent.EXTRA_EMAIL, mail)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

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
        if (!irAComprobacion) {
            when(pantallaActual){
                Pantallas.Mail -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dame una direccion de correo")
                Pantallas.Subject -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Cual quieres que sea el tema?")
                Pantallas.Message -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Cual quieres que sea el mensaje a entregar?")
                else -> {}
            }

        }else{
            when(pantallaActual){
                Pantallas.Mail -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Seguro que quieres que tu $correo sea $mensajeEscuchado? \n Di 'Si' o 'confirmar' para aceptar")
                Pantallas.Subject -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Seguro que quieres que tu $tema sea $mensajeEscuchado? \n Di 'Si' o 'confirmar' para aceptar")
                Pantallas.Message -> intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Seguro que quieres que tu $mensaje sea $mensajeEscuchado? \n Di 'Si' o 'confirmar' para aceptar")
                else -> {}
            }
        }

        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_LONG).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toString()
                    result = result.substring(1, result.length - 1)

                    if (!irAComprobacion) {
                        mensajeEscuchado = result
                        textViewTexto.text = mensajeEscuchado
                        irAComprobacion = true
                    } else {
                        elMensajeHaSidoComprobado = result.equals("si", ignoreCase = true)
                        irAComprobacion = false
                    }

                    // ESTO ES PARA VER SI EL MENSAJE SE RECOGE CORRECTAMENTE, ELIMINAR CUANDO ENTREGUEMOS
                    Log.i("Mensaje", mensajeEscuchado) // filtrar por Mensaje en el logcat para ver este log correctamente
                }
            }
        }
    }
}