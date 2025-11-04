package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.button.MaterialButton
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestRotinaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_rotina)

        val botaoProxima = findViewById<MaterialButton>(R.id.btnProxima2)


        botaoProxima.setOnClickListener {


            val intent = Intent(this, CapturaImagemActivity::class.java)


            startActivity(intent)
        }




        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)


        botaoVoltar.setOnClickListener {


            finish()
        }
    }
}