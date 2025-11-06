package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class QuestPeleActivity : AppCompatActivity() {

    private lateinit var cardPeleNormal: MaterialCardView
    private lateinit var cardPeleOleosa: MaterialCardView
    private lateinit var cardPeleSeca: MaterialCardView
    private lateinit var cardPeleMista: MaterialCardView
    private lateinit var botaoProxima: MaterialButton

    private lateinit var allCards: List<MaterialCardView>

    // --- Variável para guardar a resposta ---
    private var respostaPele: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_pele)


        botaoProxima = findViewById(R.id.btnProxima2)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)

        cardPeleNormal = findViewById(R.id.cardPeleNormal)
        cardPeleOleosa = findViewById(R.id.cardPeleOleosa)
        cardPeleSeca = findViewById(R.id.cardPeleSeca)
        cardPeleMista = findViewById(R.id.cardPeleMista)


        allCards = listOf(cardPeleNormal, cardPeleOleosa, cardPeleSeca, cardPeleMista)


        botaoProxima.isEnabled = false


        cardPeleNormal.setOnClickListener {
            handleCardSelection(it as MaterialCardView, "NORMAL")
        }
        cardPeleOleosa.setOnClickListener {
            handleCardSelection(it as MaterialCardView, "OLEOSA")
        }
        cardPeleSeca.setOnClickListener {
            handleCardSelection(it as MaterialCardView, "SECA")
        }
        cardPeleMista.setOnClickListener {
            handleCardSelection(it as MaterialCardView, "MISTA")
        }


        botaoProxima.setOnClickListener {

            if (respostaPele != null) {

                val questions = ArrayList<QuizQuestion>()

                questions.add(QuizQuestion("Como você classificaria sua pele?", respostaPele!!))

                val intent = Intent(this, QuestFaixaEtariaActivity::class.java)

                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)

                startActivity(intent)
            }
        }

        botaoVoltar.setOnClickListener {
            finish()
        }
    }

    private fun handleCardSelection(selectedCard: MaterialCardView, resposta: String) {

        val corLaranja = ContextCompat.getColor(this, R.color.dermage_orange)
        val corCinza = ContextCompat.getColor(this, R.color.card_border_gray) // Ex: #DDDDDD

        allCards.forEach { card ->
            card.strokeColor = corCinza
            card.strokeWidth = 1
        }

        selectedCard.strokeColor = corLaranja
        selectedCard.strokeWidth = 2

        respostaPele = resposta

        botaoProxima.isEnabled = true
    }
}