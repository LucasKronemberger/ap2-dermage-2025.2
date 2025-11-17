package com.example.appdermageofc

import android.content.Intent
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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.util.Locale

class ResultadoActivity : AppCompatActivity() {


    private lateinit var containerManha: LinearLayout
    private lateinit var containerNoite: LinearLayout

    private lateinit var btnManha: Button
    private lateinit var btnNoite: Button
    private var corFundoAtivo: Int = 0
    private var corFundoInativo: Int = 0
    private var corTextoAtivo: Int = 0
    private var corTextoInativo: Int = 0
    private var corBordaInativa: Int = 0
    private var corRosa: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val contentWrapper = v.findViewById<LinearLayout>(R.id.main_content_wrapper)
            contentWrapper.setPadding(
                contentWrapper.paddingLeft,
                systemBars.top,
                contentWrapper.paddingRight,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }


        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("ANALYSIS_RESULT", AnalysisResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<AnalysisResponse>("ANALYSIS_RESULT")
        }


        val tvTituloResultado = findViewById<TextView>(R.id.tv_titulo_resultado)
        val tvConcerns = findViewById<TextView>(R.id.tv_concerns)
        val btnHome = findViewById<Button>(R.id.btn_home)


        val sliderAcne = findViewById<SeekBar>(R.id.slider_acne)
        val sliderHidratacao = findViewById<SeekBar>(R.id.slider_hidratacao)
        val sliderManchas = findViewById<SeekBar>(R.id.slider_manchas)
        val sliderRugas = findViewById<SeekBar>(R.id.slider_rugas)
        val sliderPoros = findViewById<SeekBar>(R.id.slider_poros)


        val labelAcne = findViewById<TextView>(R.id.label_acne)
        val labelHidratacao = findViewById<TextView>(R.id.label_hidratacao)
        val labelManchas = findViewById<TextView>(R.id.label_manchas)
        val labelRugas = findViewById<TextView>(R.id.label_rugas)
        val labelPoros = findViewById<TextView>(R.id.label_poros)


        containerManha = findViewById(R.id.container_rotina_manha)
        containerNoite = findViewById(R.id.container_rotina_noite)
        btnManha = findViewById(R.id.btn_manha)
        btnNoite = findViewById(R.id.btn_noite)


        corFundoAtivo = ContextCompat.getColor(this, R.color.white)
        corTextoAtivo = ContextCompat.getColor(this, R.color.black)
        corFundoInativo = ContextCompat.getColor(this, R.color.white)
        corTextoInativo = ContextCompat.getColor(this, R.color.dermage_gray)
        corBordaInativa = ContextCompat.getColor(this, R.color.card_border_gray)
        corRosa = ContextCompat.getColor(this, R.color.dermage_pink)


        if (result != null) {


            tvConcerns.text = "PRINCIPAIS PREOCUPAÇÕES\n${result.concerns}"

            popularSlider(sliderAcne, labelAcne, result.scores, "Acne")
            popularSlider(sliderHidratacao, labelHidratacao, result.scores, "Hidratação")
            popularSlider(sliderManchas, labelManchas, result.scores, "Manchas")
            popularSlider(sliderRugas, labelRugas, result.scores, "Rugas")
            popularSlider(sliderPoros, labelPoros, result.scores, "Poros")


            containerManha.removeAllViews()
            containerNoite.removeAllViews()


            popularRotina(containerManha, result.routine.morning, "MANHÃ")
            popularRotina(containerNoite, result.routine.night, "NOITE")


            setupTabs()

            mostrarRotinaManha()

        } else {

            tvTituloResultado.text = "Erro ao carregar resultados"
            tvConcerns.text = "Não foi possível carregar a análise. Tente novamente."
        }


        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    private fun popularSlider(seekBar: SeekBar, label: TextView, scores: List<SkinScore>, tagName: String) {

        val score = scores.find { it.scoreTag.equals(tagName, ignoreCase = true) }

        if (score != null) {

            seekBar.progress = (score.scoreNumber * 10).toInt()
        } else {

            label.visibility = View.GONE
            seekBar.visibility = View.GONE
        }

        seekBar.isEnabled = false
    }

    private fun setupTabs() {
        btnManha.setOnClickListener { mostrarRotinaManha() }
        btnNoite.setOnClickListener { mostrarRotinaNoite() }
    }

    private fun mostrarRotinaManha() {
        containerManha.visibility = View.VISIBLE
        containerNoite.visibility = View.GONE

        btnManha.setBackgroundColor(corFundoAtivo)
        btnManha.setTextColor(corTextoAtivo)
        (btnManha as MaterialButton).strokeWidth = 2
        (btnManha as MaterialButton).strokeColor = ContextCompat.getColorStateList(this, R.color.black)

        btnNoite.setBackgroundColor(corRosa)
        btnNoite.setTextColor(corFundoAtivo) // Texto branco
        (btnNoite as MaterialButton).strokeWidth = 0
    }

    private fun mostrarRotinaNoite() {
        containerManha.visibility = View.GONE
        containerNoite.visibility = View.VISIBLE

        btnNoite.setBackgroundColor(corRosa)
        btnNoite.setTextColor(corFundoAtivo)
        (btnNoite as MaterialButton).strokeWidth = 0

        btnManha.setBackgroundColor(corFundoInativo)
        btnManha.setTextColor(corTextoInativo)
        (btnManha as MaterialButton).strokeWidth = 1
        (btnManha as MaterialButton).strokeColor = ContextCompat.getColorStateList(this, R.color.card_border_gray)
    }

    private fun popularRotina(container: LinearLayout, produtos: List<SkinCareProduct>, passoTitulo: String) {
        val inflater = LayoutInflater.from(this)

        if (produtos.isEmpty()) {
            val noProductView = TextView(this)
            noProductView.text = "Nenhum produto recomendado para a rotina da ${passoTitulo.lowercase(Locale.ROOT)}."
            noProductView.setPadding(0, 32, 0, 32)
            noProductView.gravity = View.TEXT_ALIGNMENT_CENTER
            container.addView(noProductView)
            return
        }


        produtos.forEachIndexed { index, produto ->
            val passoView = TextView(this)
            passoView.text = "Passo ${index + 1}"
            passoView.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6)
            passoView.setPadding(0, 24, 0, 8)
            container.addView(passoView)

            val productView = inflater.inflate(R.layout.item_produto_resultado, container, false)


            val tvTitulo = productView.findViewById<TextView>(R.id.tv_produto_titulo)
            val tvDesc = productView.findViewById<TextView>(R.id.tv_produto_desc)
            val tvPreco = productView.findViewById<TextView>(R.id.tv_produto_preco)
            val ivImagem = productView.findViewById<ImageView>(R.id.iv_produto_imagem)
            val btnComprar = productView.findViewById<Button>(R.id.btn_comprar)
            val btnVerMais = productView.findViewById<Button>(R.id.btn_ver_mais)


            tvTitulo.text = produto.title
            tvDesc.text = produto.description
            tvPreco.text = "R$ ${"%.2f".format(produto.price).replace(".", ",")}"


            Glide.with(this)
                .load(produto.imageUrl)
                .into(ivImagem)


            val linkClickListener = View.OnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(produto.link))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Não foi possível abrir o link", Toast.LENGTH_SHORT).show()
                }
            }
            btnComprar.setOnClickListener(linkClickListener)
            btnVerMais.setOnClickListener(linkClickListener)


            container.addView(productView)
        }
    }
}