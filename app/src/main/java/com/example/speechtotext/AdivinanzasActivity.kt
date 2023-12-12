package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AdivinanzasActivity : AppCompatActivity() {
    // crea una variable constante con valor 100, se usará como código para identificar la respuesta.
    private val REQ_CODE = 100

    // crea 3 variables para los componentes que se van a modificar en el layout
    private lateinit var btnDarAdivinanza : Button
    private lateinit var btnResolver : Button
    private lateinit var textResultado : TextView

    // crea 2 variables para insertar los string arrays de strings.xml
    private lateinit var adivinanzas : Array<String>
    private lateinit var listaDeRespuestas : MutableList<Array<String>>
    // crea una variable para obtener el numero de adivinanza a mostrar
    private var nAdivinanza = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //establece el contenido de la actividad
        setContentView(R.layout.activity_adivinanzas)
        // pone el fondo de la actividad de color blanco.
        window.decorView.setBackgroundColor(Color.parseColor(resources.getColor(R.color.white).toString()))

        // se inicializa obteniendo el string arrays de string.xml
        adivinanzas = resources.getStringArray(R.array.adivinanzas)
        // se inicializa obteniendo los strings arrays de string.xml
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

        // Inicializa las vistas
        textResultado = findViewById(R.id.resultado)
        btnDarAdivinanza = findViewById(R.id.empezar)
        btnResolver = findViewById(R.id.resolver)

        // crea un OnclickListener en el cual saca un numero random según el numero de adivinanzas
        btnDarAdivinanza.setOnClickListener {
            nAdivinanza = adivinanzas.indices.random()
            textResultado.text = adivinanzas[nAdivinanza]
        }

        // crea un OnclickListener que inicia el reconocimiento de voz
        btnResolver.setOnClickListener {
            iniciarReconocimientoDeVoz()
        }
    }

    private fun iniciarReconocimientoDeVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) // se crea un intent con la acción reconocer voz
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) // pone el modelo de lenguaje de reconocimiento como libre
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()) // pone el lenguaje de reconocimiento como el que está en el dispositivo
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dame tu respuesta") // Es el mensaje que muestra cuando empieza el reconocimiento

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
                    val mensajeEscuchado = result.substring(1, result.length - 1) // como el data en la variable result viene en formato [data] le quita la primera y la ultima posicion
                    if (listaDeRespuestas[nAdivinanza].contains(mensajeEscuchado.lowercase())){ // confirma si la respuesta esta dentro del array de respuestas
                        window.decorView.setBackgroundColor(Color.parseColor("#00FF00")) // pone el fondo en verde
                        textResultado.text = resources.getString(R.string.correcto) // el textResultado muestra el texto obtenido de strings.xml
                    }else{
                        window.decorView.setBackgroundColor(Color.parseColor("#FF0000")) // pone el fondo en rojo
                        textResultado.text = resources.getString(R.string.incorrecto) // el textResultado muestra el texto obtenido de strings.xml
                    }
                }
            }
        }
    }
}