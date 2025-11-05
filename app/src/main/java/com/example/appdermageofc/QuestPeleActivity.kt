package com.example.appdermageofc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class QuestPeleActivity : AppCompatActivity() {

    // --- Variáveis para guardar os componentes ---
    private lateinit var cardPeleNormal: MaterialCardView
    private lateinit var cardPeleOleosa: MaterialCardView
    private lateinit var cardPeleSeca: MaterialCardView
    private lateinit var cardPeleMista: MaterialCardView
    private lateinit var botaoProxima: MaterialButton

    // Lista com todos os cards para facilitar a lógica de "desselecionar"
    private lateinit var allCards: List<MaterialCardView>

    // --- Variável para guardar a resposta ---
    private var respostaPele: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_pele)

        // --- 1. Encontrar todos os componentes ---
        botaoProxima = findViewById(R.id.btnProxima2)
        val botaoVoltar = findViewById<ImageButton>(R.id.setaBack)

        cardPeleNormal = findViewById(R.id.cardPeleNormal)
        cardPeleOleosa = findViewById(R.id.cardPeleOleosa)
        cardPeleSeca = findViewById(R.id.cardPeleSeca)
        cardPeleMista = findViewById(R.id.cardPeleMista)

        // Coloca todos em uma lista
        allCards = listOf(cardPeleNormal, cardPeleOleosa, cardPeleSeca, cardPeleMista)

        // --- 2. Desabilitar o botão "Próxima" no início ---
        botaoProxima.isEnabled = false

        // --- 3. Configurar os cliques dos CARDS ---
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

        // --- 4. Configurar o clique do botão "Próxima" ---
        botaoProxima.setOnClickListener {
            // Só executa se uma resposta tiver sido selecionada
            if (respostaPele != null) {

                // 1. Crie a lista de perguntas
                val questions = ArrayList<QuizQuestion>()

                // 2. Adicione a resposta REAL desta tela
                // O "!!
                questions.add(QuizQuestion("Como você classificaria sua pele?", respostaPele!!))

                // 3. Crie a Intent
                val intent = Intent(this, QuestFaixaEtariaActivity::class.java)

                // 4. PASSE A LISTA DE PERGUNTAS PARA A PRÓXIMA TELA
                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)

                startActivity(intent)
            }
        }

        // --- 5. Configurar o clique do botão "Voltar" ---
        botaoVoltar.setOnClickListener {
            finish()
        }
    }

    /**
     * Função para gerenciar a seleção visual dos cards.
     */
    private fun handleCardSelection(selectedCard: MaterialCardView, resposta: String) {

        // Pega as cores (precisa ter as cores definidas em colors.xml)
        val corLaranja = ContextCompat.getColor(this, R.color.dermage_orange)
        val corCinza = ContextCompat.getColor(this, R.color.card_border_gray) // Ex: #DDDDDD

        // 1. Reseta a borda de TODOS os cards
        allCards.forEach { card ->
            card.strokeColor = corCinza
            card.strokeWidth = 1 // Garante que a borda volte ao normal
        }

        // 2. Destaca o card SELECIONADO
        selectedCard.strokeColor = corLaranja
        selectedCard.strokeWidth = 2 // Borda mais grossa para destacar

        // 3. Armazena a resposta
        respostaPele = resposta

        // 4. Habilita o botão "Próxima"
        botaoProxima.isEnabled = true
    }
}