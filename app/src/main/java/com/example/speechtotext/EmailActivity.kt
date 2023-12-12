package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class EmailActivity : AppCompatActivity() {
    // crea una variable constante con valor 100, se usará como código para identificar la respuesta.
    private val REQ_CODE = 100
    // crea 3 variables para los componentes que se van a modificar en el layout
    private lateinit var textViewCorreo: TextView
    private lateinit var textViewSubject: TextView
    private lateinit var textViewMessage: TextView
    private lateinit var btnSend: Button
    // otras variables
    private lateinit var mensajeEscuchado: String
    private lateinit var pedir: String
    private lateinit var pedirSubject : String
    private lateinit var pedirMsg : String
    // variables que se van a pasar al correo
    private val correo = "ejemplo@gmail.com"
    private var tema = ""
    private var mensaje = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //establece el contenido de la actividad
        setContentView(R.layout.activity_email)
        // inicializar las vistas
        textViewCorreo = findViewById(R.id.TextViewCorreo)
        textViewCorreo.text = correo
        textViewSubject = findViewById(R.id.TextViewSubject)
        textViewMessage = findViewById(R.id.TextViewMessage)
        btnSend = findViewById(R.id.button)
        pedirSubject = resources.getString(R.string.pedirSubject)
        pedirMsg = resources.getString(R.string.pedirMsg)

        pedirSubject() // lama al metodo pedirSubject
    }

    private fun pedirSubject() {
        // cambia la variable pedir por el valor de pedirSubject
        pedir = pedirSubject
        iniciarReconocimientoDeVoz() //inicia la funcion de reconocimiento
    }

    private fun pedirMessage() {
        pedir = pedirMsg // cambia la variable pedir por el valor de pedirMsg
        iniciarReconocimientoDeVoz() //inicia la funcion de reconocimiento
    }

    private fun mailActivity() {
        textViewSubject.text = tema // cambia el texto que se muestra en textViewSubject por el contenido de tema
        textViewMessage.text = mensaje // cambia el texto que se muestra en textViewSubject por el contenido de mensaje
        // crea un onclicklistener que inicia la funcion sendMail
        btnSend.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        // crea un intent con la accion de enviar
        intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:") // establece el esquema como "mailto:"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correo)) // pone como email extra el contenido de correo
        intent.putExtra(Intent.EXTRA_SUBJECT, tema) // pone como tema extra el contenido de tema
        intent.putExtra(Intent.EXTRA_TEXT, mensaje) // pone como mensaje extra el contenido de mensaje
        intent.type = "message/rfc822"
        // inicia el intent
        startActivity(Intent.createChooser(intent, "Elige una aplicación para enviar el mensaje"))
        if (intent.resolveActivity(packageManager) != null) { // comprueba si tu dispositivo tiene un package manager
            startActivity(intent)
        } else {
            Toast.makeText(this, "No hay aplicaciones de correo disponibles", Toast.LENGTH_SHORT).show()
        }
    }

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) // se crea un intent con la acción reconocer voz
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) // pone el modelo de lenguaje de reconocimiento como libre
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()) // pone el lenguaje de reconocimiento como el que está en el dispositivo
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, pedir)  // Es el mensaje que muestra cuando empieza el reconocimiento

        try { // intenta iniciar la actividad de reconocimiento con el código RQE_CODE
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) { // si el dispositivo no soporta el reconocimiento te salta este mensaje
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) { //confirma si la aplicacion devuelve el ok y algo en el data
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) // obtiene el data
            if (!results.isNullOrEmpty()) { // evita que results este vacio
                mensajeEscuchado = results[0]

                if (pedir == pedirSubject) { // comprueba si lo pedido es el tema o el mensaje
                    tema = mensajeEscuchado
                    pedirMessage() // incia la funcion pedir mensaje
                } else if (pedir == pedirMsg) {
                    mensaje = mensajeEscuchado
                    mailActivity() // inicia la funcion mail activity
                }
            }
        }
    }
}