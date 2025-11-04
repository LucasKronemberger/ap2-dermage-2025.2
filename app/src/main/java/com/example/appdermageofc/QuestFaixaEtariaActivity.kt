package com.example.appdermageofc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.widget.ImageButton
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestFaixaEtariaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_faixa_etaria)

        val botaoProxima = findViewById<Button>(R.id.btnProxima2)

        botaoProxima.setOnClickListener {
            val intent = Intent(this, QuestPreocupacaoActivity::class.java)
            startActivity(intent)
        }

        val botaoVoltar = findViewById<ImageButton>(R.id.BtnBack)

        botaoVoltar.setOnClickListener {
            finish()
        }

    }
}