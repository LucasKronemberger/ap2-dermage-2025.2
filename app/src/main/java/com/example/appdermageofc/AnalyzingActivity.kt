package com.example.appdermageofc

import android.content.Intent  // 👈 ADICIONE ESTE IMPORT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AnalyzingActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    private val updateRunnable = object : Runnable {
        override fun run() {
            // Garante que a progressBar foi inicializada
            // (Boa prática, mantenha isso)
            if (!::progressBar.isInitialized) return

            if (progress <= 100) {
                // Atualiza o progresso
                progressBar.progress = progress
                progressText.text = getString(R.string.progresso_template, progress)
                progress += 1 // Incrementa
                handler.postDelayed(this, 50L) // Roda de novo após 50ms
            } else {
                // --- O PROGRESSO CHEGOU A 100% ---
                handler.removeCallbacks(this) // Para o loop

                // 1. Crie a Intent para a tela de Resultado
                val intent = Intent(this@AnalyzingActivity, ResultadoActivity::class.java)

                // 2. Inicie a tela de Resultado
                startActivity(intent)

                // 3. Feche a tela de "Analyzing"
                // Isso impede o usuário de apertar "Voltar" e cair
                // numa tela de loading já terminada.
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analyzing)

        progressBar = findViewById(R.id.progressBarLinear)
        progressText = findViewById(R.id.textProgress)

        // Inicia o processo de simulação de carregamento
        handler.post(updateRunnable)
    }

    override fun onDestroy() {
        // Boa prática: remove os callbacks se a tela for destruída
        handler.removeCallbacks(updateRunnable)
        super.onDestroy()
    }
}