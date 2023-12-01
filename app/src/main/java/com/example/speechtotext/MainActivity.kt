package com.example.speechtotext

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private val REQ_CODE = 100
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        val speak: ImageView = findViewById(R.id.speak)

        speak.setOnClickListener {
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
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toString()
                    result = result.substring(1, result.length - 1) // Se hace un substring porque viene en formato [texto] y le queremos quitar los corchetes

                    // ESTO ES PARA VER SI EL MENSAJE SE RECOGE CORRECTAMENTE, ELIMINAR CUANDO ENTREGUEMOS
                    Log.i("Mensaje", result) // filtrar por Mensaje en el logcat

                    intent = Intent(this@MainActivity, EmailActivity::class.java)
                    intent.putExtra("MENSAJE", result)
                    startActivity(intent)
                }
            }
        }
    }
}