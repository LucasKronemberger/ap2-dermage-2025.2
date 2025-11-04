package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestPreocupacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_preocupacao)

        val botaoProxima = findViewById<Button>(R.id.btnProxima2)

        botaoProxima.setOnClickListener {
            val intent = Intent(this, QuestRotinaActivity::class.java)
            startActivity(intent)
        }

        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)

        botaoVoltar.setOnClickListener {
            finish()
        }
    }
}