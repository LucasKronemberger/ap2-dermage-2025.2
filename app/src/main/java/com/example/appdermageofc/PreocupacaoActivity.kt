package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PreocupacaoActivity : AppCompatActivity() {

    private var questions: ArrayList<QuizQuestion>? = null
    private var thisQuestionText: String = ""
    private var thisAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preocupacao)

        // 1. Receber lista de perguntas anteriores
        questions = intent.getParcelableArrayListExtra("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList()
        }

        // 2. Vincular componentes da UI
        val botaoProxima = findViewById<MaterialButton>(R.id.btnProxima)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupPreocupacao)
        val tvTitulo = findViewById<TextView>(R.id.tvPreocupacaoTitulo)

        thisQuestionText = tvTitulo.text.toString()
        botaoProxima.isEnabled = false

        // 3. Lógica de Seleção
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                thisAnswer = when (checkedId) {
                    R.id.rb_manchas -> "Manchas/Melasma"
                    R.id.rb_acne -> "Acne Ativa"
                    R.id.rb_poros -> "Poros e Textura"
                    R.id.rb_desidratacao -> "Desidratação/Opacidade"
                    R.id.rb_nenhuma_preocupacao -> "Nenhuma preocupação específica"
                    else -> "Não informado"
                }
                botaoProxima.isEnabled = true
            }
        }

        // 4. Ação do Botão Próxima
        botaoProxima.setOnClickListener {
            if (thisAnswer != null) {
                // Salva a resposta final
                questions?.add(QuizQuestion(thisQuestionText, thisAnswer!!))

                // --- FINALIZAÇÃO DO QUESTIONÁRIO ---
                // Agora o fluxo vai para a tela de Captura de Imagem (CameraX/Foto)
                val intent = Intent(this, CapturaImagemActivity::class.java)

                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, selecione uma opção", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. Botão Voltar
        botaoVoltar.setOnClickListener {
            finish()
        }
    }
}