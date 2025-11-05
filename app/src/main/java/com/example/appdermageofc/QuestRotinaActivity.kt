package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestRotinaActivity : AppCompatActivity() {

    private var questions: ArrayList<QuizQuestion>? = null
    private var thisQuestionText: String = ""
    private var thisAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_rotina)

        questions = intent.getParcelableArrayListExtra<QuizQuestion>("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList()
        }


        val botaoProxima = findViewById<MaterialButton>(R.id.btnProxima2)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupRotina)
        val tvTitulo = findViewById<TextView>(R.id.tvqualrotina)


        thisQuestionText = tvTitulo.text.toString()


        botaoProxima.isEnabled = false


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {

                val selectedRadioButton = findViewById<RadioButton>(checkedId)

                thisAnswer = selectedRadioButton.text.toString()


                botaoProxima.isEnabled = true
            }
        }


        botaoProxima.setOnClickListener {

            if (thisAnswer != null) {

                questions?.add(QuizQuestion(thisQuestionText, thisAnswer!!))

                val intent = Intent(this, CapturaImagemActivity::class.java)


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