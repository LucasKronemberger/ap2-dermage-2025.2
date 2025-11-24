package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SensibilidadeActivity : AppCompatActivity() {

    private var questions: ArrayList<QuizQuestion>? = null
    private var thisQuestionText: String = ""
    private var thisAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensibilidade)

        // Recebe os dados da Rotina
        questions = intent.getParcelableArrayListExtra("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList()
        }

        val botaoProxima = findViewById<MaterialButton>(R.id.btnProxima)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupSensibilidade)
        val tvTitulo = findViewById<TextView>(R.id.tvSensibilidadeTitulo)

        thisQuestionText = tvTitulo.text.toString()
        botaoProxima.isEnabled = false

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                thisAnswer = when (checkedId) {
                    R.id.rb_nao_sensivel -> "Não sensível"
                    R.id.rb_levemente_sensivel -> "Ligeiramente sensível"
                    R.id.rb_sensivel -> "Sensível"
                    R.id.rb_muito_sensivel -> "Muito Sensível / Rosácea"
                    else -> "Não informado"
                }
                botaoProxima.isEnabled = true
            }
        }

        botaoProxima.setOnClickListener {
            if (thisAnswer != null) {
                questions?.add(QuizQuestion(thisQuestionText, thisAnswer!!))

                // --- CORREÇÃO FEITA AQUI ---
                // O fluxo deve seguir para PreocupacaoActivity (Passo 6 - Específica)
                // e não voltar para QuestPreocupacaoActivity (Passo 3 - Envelhecimento)
                val intent = Intent(this, PreocupacaoActivity::class.java)

                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecione uma opção", Toast.LENGTH_SHORT).show()
            }
        }

        botaoVoltar.setOnClickListener {
            finish()
        }
    }
}