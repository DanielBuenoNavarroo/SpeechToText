package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class AdivinanzasActivity : AppCompatActivity() {
    private val REQ_CODE = 100

    private lateinit var btnDarAdivinanza : Button
    private lateinit var btnResolver : Button
    private lateinit var textResultado : TextView

    private lateinit var adivinanzas : Array<String>
    private lateinit var listaDeRespuestas : MutableList<Array<String>>
    private var nAdivinanza = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adivinanzas)
        window.decorView.setBackgroundColor(Color.parseColor("#FFFFFF"))

        adivinanzas = resources.getStringArray(R.array.adivinanzas)
        listaDeRespuestas = mutableListOf(
            resources.getStringArray(R.array.respuesta1),
            resources.getStringArray(R.array.respuesta2),
            resources.getStringArray(R.array.respuesta3),
            resources.getStringArray(R.array.respuesta4),
            resources.getStringArray(R.array.respuesta5),
            resources.getStringArray(R.array.respuesta6),
            resources.getStringArray(R.array.respuesta7),
            resources.getStringArray(R.array.respuesta8),
            resources.getStringArray(R.array.respuesta9),
            resources.getStringArray(R.array.respuesta10)
        )

        textResultado = findViewById(R.id.resultado)
        btnDarAdivinanza = findViewById(R.id.darAdivinanza)
        btnResolver = findViewById(R.id.resolver)

        btnDarAdivinanza.setOnClickListener {
            nAdivinanza = adivinanzas.indices.random()
            textResultado.text = adivinanzas[nAdivinanza]
        }

        btnResolver.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }
    }

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, adivinanzas[nAdivinanza])

        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toString()
                    val mensajeEscuchado = result.substring(1, result.length - 1)
                    if (listaDeRespuestas[nAdivinanza].contains(mensajeEscuchado.lowercase())){
                        Log.i("mensg", "si")
                        window.decorView.setBackgroundColor(Color.parseColor("#00FF00"))
                        textResultado.text = "Correcto!!! :D"
                    }else{
                        Log.i("mensg", "no")
                        window.decorView.setBackgroundColor(Color.parseColor("#FF0000"))
                        textResultado.text = "Incorrecto!!! :("
                    }
                }
            }
        }
    }
}