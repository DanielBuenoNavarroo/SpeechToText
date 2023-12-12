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
    // crea una variable constante con valor 100, se usará como código para identificar la respuesta.
    private val REQ_CODE = 100
    // crea 2 variables para los componentes que se van a modificar en el layout
    private lateinit var cambiarBgBtn : Button
    private lateinit var mensajeEscuchado : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //establece el contenido de la actividad
        setContentView(R.layout.activity_background)
        // inicializa la vista
        cambiarBgBtn = findViewById(R.id.bgChangeButton)
        // crea un OnclickListener que inicia la funcion de reconocimiento de voz
        cambiarBgBtn.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }
    }

    private fun cambiarBg(){
        // https://htmlcolorcodes.com/
        val color = when(mensajeEscuchado.lowercase()){ // según el mensaje escuchado cambia el color de fondo
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
            else -> "0xE6E6FA" // esta opcion es para si no escucha ninguno de los anteriores
        }
        window.decorView.setBackgroundColor(Color.parseColor(color))
    }

    private fun iniciarReconocimientoDeVoz() {
        // se crea un intent con la acción reconocer voz
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // pone el modelo de lenguaje de reconocimiento como libre
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        // pone el lenguaje de reconocimiento como el que está en el dispositivo
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        // Es el mensaje que muestra cuando empieza el reconocimiento
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Elige un color")

        try { // intenta iniciar la actividad de reconocimiento con el código RQE_CODE
            startActivityForResult(intent, REQ_CODE)
        } catch (e: ActivityNotFoundException) { // si el dispositivo no soporta el reconocimiento te salta este mensaje
            Toast.makeText(applicationContext, "Sorry your device not supported", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) { //confirma si la aplicacion devuelve el ok y algo en el data
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toString() // obtiene el data y lo convierte en string
                    mensajeEscuchado = result.substring(1, result.length - 1)  // confirma si la respuesta esta dentro del array de respuestas
                    cambiarBg() // llama al metodo cambiarBg
                }
            }
        }
    }
}