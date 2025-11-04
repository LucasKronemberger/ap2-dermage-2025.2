package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val botaoAnalise = findViewById<ImageButton>(R.id.btAnalisePele)

        botaoAnalise.setOnClickListener {
            val intent = Intent(this, ConsentimentoPesquisaActivity::class.java)
            startActivity(intent)
        }
    }
}