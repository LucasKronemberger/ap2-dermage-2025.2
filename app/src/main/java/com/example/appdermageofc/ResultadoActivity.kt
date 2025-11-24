package com.example.appdermageofc

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.util.Locale

class ResultadoActivity : AppCompatActivity() {

    // Componentes de Layout
    private lateinit var containerManha: LinearLayout
    private lateinit var containerNoite: LinearLayout
    private lateinit var btnManha: MaterialButton
    private lateinit var btnNoite: MaterialButton

    // Cores (Definidas no código para facilitar a alternância)
    private val corLaranja = Color.parseColor("#F39A08")
    private val corBranca = Color.WHITE
    private val corFundoInativo = Color.parseColor("#F0F0F0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // 1. Receber Dados da API
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("ANALYSIS_RESULT", AnalysisResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<AnalysisResponse>("ANALYSIS_RESULT")
        }

        // 2. Vincular Componentes
        val tvTituloResultado = findViewById<TextView>(R.id.tv_titulo_resultado)
        val tvConcerns = findViewById<TextView>(R.id.tv_concerns)
        val btnHome = findViewById<MaterialButton>(R.id.btn_home)
        val btnComprar = findViewById<MaterialButton>(R.id.btn_comprar_rotina)

        // Containers e Abas
        containerManha = findViewById(R.id.container_rotina_manha)
        containerNoite = findViewById(R.id.container_rotina_noite)
        btnManha = findViewById(R.id.btn_manha)
        btnNoite = findViewById(R.id.btn_noite)

        // 3. Preencher Dados na Tela
        if (result != null) {

            // Texto de Diagnóstico
            tvConcerns.text = "Diagnóstico: ${result.concerns}\n\nRecomendamos a rotina abaixo para equilibrar sua pele."

            // Popular Sliders (Notas)
            popularSlider(findViewById(R.id.slider_acne), result.scores, "Acne")
            popularSlider(findViewById(R.id.slider_hidratacao), result.scores, "Hidratação")
            popularSlider(findViewById(R.id.slider_manchas), result.scores, "Manchas")
            popularSlider(findViewById(R.id.slider_rugas), result.scores, "Rugas")
            popularSlider(findViewById(R.id.slider_poros), result.scores, "Poros")

            // Limpar containers antes de adicionar
            containerManha.removeAllViews()
            containerNoite.removeAllViews()

            // Popular Listas de Produtos
            popularRotina(containerManha, result.routine.morning, "MANHÃ")
            popularRotina(containerNoite, result.routine.night, "NOITE")

            // Configurar botões de aba
            setupTabs()

            // Iniciar na aba Manhã
            atualizarAbas(ehManha = true)

        } else {
            tvTituloResultado.text = "Erro na Análise"
            tvConcerns.text = "Não foi possível carregar seus resultados. Tente novamente."
        }

        // 4. Botão Voltar para Home
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Botão Comprar (Link Genérico ou Específico)
        btnComprar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dermage.com.br/"))
            startActivity(intent)
        }
    }

    // --- LÓGICA DE ABAS ---
    private fun setupTabs() {
        btnManha.setOnClickListener { atualizarAbas(ehManha = true) }
        btnNoite.setOnClickListener { atualizarAbas(ehManha = false) }
    }

    private fun atualizarAbas(ehManha: Boolean) {
        if (ehManha) {
            // Visual Manhã ATIVO
            containerManha.visibility = View.VISIBLE
            containerNoite.visibility = View.GONE

            // Estilo Manhã (Preenchido)
            btnManha.setBackgroundColor(corLaranja)
            btnManha.setTextColor(corBranca)
            btnManha.strokeWidth = 0

            // Estilo Noite (Borda)
            btnNoite.setBackgroundColor(corFundoInativo)
            btnNoite.setTextColor(corLaranja)
            btnNoite.setStrokeColor(android.content.res.ColorStateList.valueOf(corLaranja))
            btnNoite.strokeWidth = 3 // Borda visível
        } else {
            // Visual Noite ATIVO
            containerManha.visibility = View.GONE
            containerNoite.visibility = View.VISIBLE

            // Estilo Manhã (Borda)
            btnManha.setBackgroundColor(corFundoInativo)
            btnManha.setTextColor(corLaranja)
            btnManha.setStrokeColor(android.content.res.ColorStateList.valueOf(corLaranja))
            btnManha.strokeWidth = 3

            // Estilo Noite (Preenchido)
            btnNoite.setBackgroundColor(corLaranja)
            btnNoite.setTextColor(corBranca)
            btnNoite.strokeWidth = 0
        }
    }

    // --- LÓGICA DE DADOS ---
    private fun popularSlider(seekBar: SeekBar, scores: List<SkinScore>, tagName: String) {
        // Procura a nota na lista que veio da API
        val score = scores.find { it.scoreTag.contains(tagName, ignoreCase = true) }

        if (score != null) {
            // A API geralmente retorna 0.0 a 10.0 ou 0 a 100
            // Assumindo que o SeekBar max é 100
            var valor = score.scoreNumber
            if (valor <= 10) valor *= 10 // Ajuste de escala se vier pequeno

            seekBar.progress = valor.toInt()
        } else {
            // Se não achar, deixa zerado ou esconde
            seekBar.progress = 0
        }
        // Trava o slider para o usuário não mexer
        seekBar.isEnabled = false
    }

    private fun popularRotina(container: LinearLayout, produtos: List<SkinCareProduct>, periodo: String) {
        val inflater = LayoutInflater.from(this)

        if (produtos.isEmpty()) {
            val tvVazio = TextView(this)
            tvVazio.text = "Nenhum produto específico para $periodo."
            tvVazio.setPadding(16, 32, 16, 32)
            container.addView(tvVazio)
            return
        }

        produtos.forEachIndexed { index, produto ->
            // Infla o layout do item (item_produto_resultado.xml)
            // Certifique-se que você tem esse arquivo XML
            val itemView = inflater.inflate(R.layout.item_produto_resultado, container, false)


            val tvTitulo = itemView.findViewById<TextView>(R.id.tv_produto_titulo)
            val tvDesc = itemView.findViewById<TextView>(R.id.tv_produto_desc)
            val tvPreco = itemView.findViewById<TextView>(R.id.tv_produto_preco)
            val ivImagem = itemView.findViewById<ImageView>(R.id.iv_produto_imagem)
            val btnVerMais = itemView.findViewById<Button>(R.id.btn_ver_mais)

            // Preenche os dados
            // Verifica se o ID tv_produto_passo existe no seu XML do item, se não, remove essa linha

            tvTitulo.text = produto.title
            tvDesc.text = produto.description

            // Formatação de preço segura
            val precoFormatado = try {
                "R$ ${"%.2f".format(produto.price).replace(".", ",")}"
            } catch (e: Exception) { "R$ ${produto.price}" }
            tvPreco.text = precoFormatado

            // Carrega imagem com Glide
            Glide.with(this)
                .load(produto.imageUrl)
                .placeholder(R.drawable.dermagelogotype) // Imagem enquanto carrega
                .error(R.drawable.dermagelogotype) // Imagem se der erro
                .into(ivImagem)

            // Clique no botão do produto
            btnVerMais.setOnClickListener {
                if (!produto.link.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(produto.link))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Produto indisponível no site", Toast.LENGTH_SHORT).show()
                }
            }

            container.addView(itemView)
        }
    }
}