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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.util.Locale

class ResultadoActivity : AppCompatActivity() {

    // Componentes de Layout
    private lateinit var containerManha: LinearLayout
    private lateinit var containerNoite: LinearLayout
    private lateinit var btnManha: MaterialButton
    private lateinit var btnNoite: MaterialButton

    // Cores
    private var corRosa: Int = 0
    private var corBranca: Int = 0
    private var corFundoInativo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Inicializar cores
        corRosa = ContextCompat.getColor(this, R.color.dermage_pink)
        corBranca = Color.WHITE
        corFundoInativo = Color.parseColor("#F0F0F0")

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
            // Texto de diagnóstico (Vem da API ou montado)
            tvConcerns.text = result.concerns

            // Popular Sliders (Agora são 5)
            popularSlider(findViewById(R.id.slider_hidratacao), findViewById(R.id.label_hidratacao), result.scores, "Hidratação")
            popularSlider(findViewById(R.id.slider_acne), findViewById(R.id.label_acne), result.scores, "Acne")
            popularSlider(findViewById(R.id.slider_manchas), findViewById(R.id.label_manchas), result.scores, "Manchas")
            popularSlider(findViewById(R.id.slider_rugas), findViewById(R.id.label_rugas), result.scores, "Rugas")
            // Nota: Verifique se sua API retorna "Oleosidade" ou "Oiliness".
            // Caso contrário, mapeie corretamente no parâmetro tagName.
            popularSlider(findViewById(R.id.slider_oleosidade), findViewById(R.id.label_oleosidade), result.scores, "Oleosidade")

            // Limpar e Popular Listas de Produtos
            containerManha.removeAllViews()
            containerNoite.removeAllViews()
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

        // 4. Navegação
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnComprar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dermage.com.br/"))
            startActivity(intent)
        }
    }

    // --- LÓGICA DE ABAS (ROSA QUANDO ATIVO) ---
    private fun setupTabs() {
        btnManha.setOnClickListener { atualizarAbas(ehManha = true) }
        btnNoite.setOnClickListener { atualizarAbas(ehManha = false) }
    }

    private fun atualizarAbas(ehManha: Boolean) {
        if (ehManha) {
            // Manhã ATIVO (Rosa)
            containerManha.visibility = View.VISIBLE
            containerNoite.visibility = View.GONE

            btnManha.setBackgroundColor(corRosa)
            btnManha.setTextColor(corBranca)
            btnManha.strokeWidth = 0
            btnManha.iconTint = android.content.res.ColorStateList.valueOf(corBranca)

            // Noite INATIVO (Fundo Claro + Texto Rosa)
            btnNoite.setBackgroundColor(corFundoInativo)
            btnNoite.setTextColor(corRosa)
            btnNoite.setStrokeColor(android.content.res.ColorStateList.valueOf(corRosa))
            btnNoite.strokeWidth = 2
            btnNoite.iconTint = android.content.res.ColorStateList.valueOf(corRosa)
        } else {
            // Noite ATIVO (Rosa)
            containerManha.visibility = View.GONE
            containerNoite.visibility = View.VISIBLE

            // Manhã INATIVO
            btnManha.setBackgroundColor(corFundoInativo)
            btnManha.setTextColor(corRosa)
            btnManha.setStrokeColor(android.content.res.ColorStateList.valueOf(corRosa))
            btnManha.strokeWidth = 2
            btnManha.iconTint = android.content.res.ColorStateList.valueOf(corRosa)

            // Noite ATIVO
            btnNoite.setBackgroundColor(corRosa)
            btnNoite.setTextColor(corBranca)
            btnNoite.strokeWidth = 0
            btnNoite.iconTint = android.content.res.ColorStateList.valueOf(corBranca)
        }
    }

    // --- POPULAR DADOS ---
    private fun popularSlider(seekBar: SeekBar, label: TextView, scores: List<SkinScore>, tagName: String) {
        // Tenta encontrar a nota correspondente na lista (ex: "Acne", "Oiliness", etc.)
        val score = scores.find { it.scoreTag.contains(tagName, ignoreCase = true) }

        if (score != null) {
            var valor = score.scoreNumber
            // Ajuste de escala se vier de 0-10 para 0-100
            if (valor <= 10) valor *= 10
            seekBar.progress = valor.toInt()
        } else {
            // Se não achar a nota, esconde o slider para não ficar zerado feio
            // Ou pode deixar visível com 0 se preferir: seekBar.progress = 0
            label.visibility = View.GONE
            seekBar.visibility = View.GONE
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
            tvVazio.gravity = View.TEXT_ALIGNMENT_CENTER
            container.addView(tvVazio)
            return
        }

        produtos.forEachIndexed { index, produto ->
            // Usa o layout item_produto_resultado.xml
            val itemView = inflater.inflate(R.layout.item_produto_resultado, container, false)

            val tvTitulo = itemView.findViewById<TextView>(R.id.tv_produto_titulo)
            val tvDesc = itemView.findViewById<TextView>(R.id.tv_produto_desc)
            val tvPreco = itemView.findViewById<TextView>(R.id.tv_produto_preco)
            val ivImagem = itemView.findViewById<ImageView>(R.id.iv_produto_imagem)
            val btnVerMais = itemView.findViewById<Button>(R.id.btn_ver_mais)
            val btnComprarItem = itemView.findViewById<Button>(R.id.btn_comprar)

            // Preenche com segurança (?. para evitar crash se ID não existir no XML do item)
            tvTitulo?.text = produto.title
            tvDesc?.text = produto.description

            val precoFormatado = try {
                "R$ ${"%.2f".format(produto.price).replace(".", ",")}"
            } catch (e: Exception) { "R$ ${produto.price}" }
            tvPreco?.text = precoFormatado

            if (ivImagem != null) {
                Glide.with(this)
                    .load(produto.imageUrl)
                    .placeholder(R.drawable.dermagelogotype)
                    .error(R.drawable.dermagelogotype)
                    .into(ivImagem)
            }

            val linkAction = View.OnClickListener {
                if (!produto.link.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(produto.link))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Produto indisponível no site", Toast.LENGTH_SHORT).show()
                }
            }

            btnVerMais?.setOnClickListener(linkAction)
            btnComprarItem?.setOnClickListener(linkAction)

            container.addView(itemView)
        }
    }
}