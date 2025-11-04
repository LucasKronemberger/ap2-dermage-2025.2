package com.example.appdermageofc

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConsentimentoPesquisaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_consentimento_pesquisa)
        val termosTextView = findViewById<TextView>(R.id.termos_consentimento_text)
        val textoHtml = getString(R.string.termo_consentimento_analise_ia)
        val textoFormatado = HtmlCompat.fromHtml(textoHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        termosTextView.text = textoFormatado
    }
}