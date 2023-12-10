package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class BackgroundActivity : AppCompatActivity() {
    private val REQ_CODE = 100
    private lateinit var cambiarBgBtn : Button
    private lateinit var mensajeEscuchado : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        cambiarBgBtn = findViewById(R.id.bgChangeButton)
        cambiarBgBtn.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }
    }

    private fun cambiarBg(){
        // https://htmlcolorcodes.com/
        val color = when(mensajeEscuchado.lowercase()){
            "azul", "blue" -> "#0000FF"
            "verde", "green" -> "#00FF00"
            "rojo", "red" -> "#FF0000"
            "blanco", "white" -> "#FFFFFFFF"
            "negro", "black" -> "#FF000000"
            "plata", "plateado", "silver" -> "#C0C0C0"
            "gris", "grisaceo", "grey" -> "#808080"
            "naranja", "oranje", "anaranjado" -> "#800080"
            "marron", "maroon" -> "#800000"
            "amarillo", "yellow" -> "#FFFF00"
            "lima", "lime" -> "#00FF00"
            "azul verdoso", "teal" -> "#008080"
            "fuxia", "fuchsia" -> "#FF00FF"
            "morado", "purple" -> "#800080"
            else -> "0xE6E6FA" // color lavanda
        }
        window.decorView.setBackgroundColor(Color.parseColor(color))
    }

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Elige un color")

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
                    mensajeEscuchado = result.substring(1, result.length - 1)
                    cambiarBg()
                }
            }
        }
    }
}