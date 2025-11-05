package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton // 👈 Import necessário
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class QuestPreocupacaoActivity : AppCompatActivity() {

    private var questions: ArrayList<QuizQuestion>? = null

    private var thisQuestionText: String = ""

    private lateinit var botaoProxima: MaterialButton
    private lateinit var cbPoros: CheckBox
    private lateinit var cbBrilho: CheckBox
    private lateinit var cbManchas: CheckBox
    private lateinit var cbRugas: CheckBox
    private lateinit var cbAcne: CheckBox
    private lateinit var allCheckBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_preocupacao)

        // --- 1. RECEBER OS DADOS DA TELA ANTERIOR ---
        questions = intent.getParcelableArrayListExtra<QuizQuestion>("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList()
        }

        botaoProxima = findViewById(R.id.btnProxima2)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)
        val tvTitulo = findViewById<TextView>(R.id.tvpreocupacoes)
        thisQuestionText = tvTitulo.text.toString()

        cbPoros = findViewById(R.id.cb_poros)
        cbBrilho = findViewById(R.id.cb_brilho)
        cbManchas = findViewById(R.id.cb_manchas)
        cbRugas = findViewById(R.id.cb_rugas)
        cbAcne = findViewById(R.id.cb_acne)

        allCheckBoxes = listOf(cbPoros, cbBrilho, cbManchas, cbRugas, cbAcne)

        botaoProxima.isEnabled = false

        val listener = CompoundButton.OnCheckedChangeListener { _, _ ->
            updateButtonState()
        }
        allCheckBoxes.forEach { it.setOnCheckedChangeListener(listener) }



        botaoProxima.setOnClickListener {

            val selectedAnswers = mutableListOf<String>()

            // 1. Verifica quais caixas estão marcadas e coleta o texto principal
            if (cbManchas.isChecked) selectedAnswers.add("Manchas")
            if (cbBrilho.isChecked) selectedAnswers.add("Falta de Brilho")
            if (cbPoros.isChecked) selectedAnswers.add("Poros dilatados")
            if (cbRugas.isChecked) selectedAnswers.add("Rugas/Linhas")
            if (cbAcne.isChecked) selectedAnswers.add("Acnes/Espinhas")

            // 2. Junta a lista de respostas em uma única string
            val finalAnswer = selectedAnswers.joinToString(", ")

            // 3. Adiciona a resposta à lista de perguntas
            questions?.add(QuizQuestion(thisQuestionText, finalAnswer))

            //próxima tela
            val intent = Intent(this, QuestRotinaActivity::class.java)

            // 4. PASSE A LISTA ATUALIZADA ADIANTE
            intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)

            startActivity(intent)
        }


        botaoVoltar.setOnClickListener {
            finish()
        }
    }


    private fun updateButtonState() {
        val atLeastOneChecked = allCheckBoxes.any { it.isChecked }
        botaoProxima.isEnabled = atLeastOneChecked
    }
}