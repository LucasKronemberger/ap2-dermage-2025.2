package com.example.appdermageofc

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
            if (!::progressBar.isInitialized) return

            if (progress <= 100) {
                progressBar.progress = progress
                progressText.text = getString(R.string.progresso_template, progress)
                progress += 1
                handler.postDelayed(this, 50L)
            } else {
                handler.removeCallbacks(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analyzing)

        progressBar = findViewById(R.id.progressBarLinear)
        progressText = findViewById(R.id.textProgress)

        handler.post(updateRunnable)
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateRunnable)
        super.onDestroy()
    }
}