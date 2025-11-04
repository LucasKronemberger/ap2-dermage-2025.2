package com.example.appdermageofc

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import java.io.File

class CapturaImagemActivity : AppCompatActivity() {

    // Variável para guardar o caminho (Uri) da foto tirada pela câmera
    private var imageUri: Uri? = null

    // --- 1. Lançador da Galeria ---
    // Prepara um "contrato" para pegar conteúdo (uma imagem)
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Este é o "callback" - o que acontece quando o usuário seleciona uma imagem
        uri?.let {
            // Se a Uri não for nula, atualiza o ImageView
            imageUri = it // Salva a Uri para usar depois
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(it)
        }
    }

    // --- 2. Lançador da Câmera ---
    // Prepara um "contrato" para tirar uma foto
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        // Este é o "callback" - o que acontece quando o usuário tira a foto
        if (success) {
            // Se a foto foi tirada com sucesso, o "imageUri" que criamos já
            // aponta para ela. Apenas atualizamos o ImageView.
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- CÓDIGO DA TELA CHEIA (O SEU CÓDIGO) ---
        enableEdgeToEdge()
        setContentView(R.layout.activity_captura_imagem)
        val root = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        // --- FIM DO CÓDIGO DA TELA CHEIA ---


        // --- 3. Encontrar os Botões e a Imagem ---
        val botaoVoltar = findViewById<ImageButton>(R.id.btnBack)
        val botaoConfirmar = findViewById<MaterialButton>(R.id.btnConfirmar)
        val botaoPular = findViewById<TextView>(R.id.skipText)
        val imagemRosto = findViewById<ImageView>(R.id.fotoRosto)

        // --- 4. Configurar os Cliques ---

        // Clique para VOLTAR
        botaoVoltar.setOnClickListener {
            finish() // Fecha a tela atual e volta para a anterior
        }

        // Clique para PULAR ETAPA
        botaoPular.setOnClickListener {
            // Navega para a próxima tela (AnalyzingActivity) sem uma foto
            val intent = Intent(this, AnalyzingActivity::class.java)
            // Você pode querer "avisar" a próxima tela que o usuário pulou
            // intent.putExtra("ETAPA_PULADA", true)
            startActivity(intent)
        }

        // Clique para CONFIRMAR ANÁLISE
        botaoConfirmar.setOnClickListener {
            // Só navega se o usuário tiver selecionado uma imagem
            if (imageUri != null) {
                val intent = Intent(this, AnalyzingActivity::class.java)
                // Adicione a Uri da imagem na Intent para a próxima tela processar
                intent.data = imageUri
                startActivity(intent)
            } else {
                // Opcional: Mostrar um aviso se nenhuma foto foi selecionada
                // Toast.makeText(this, "Por favor, envie uma foto", Toast.LENGTH_SHORT).show()
            }
        }

        // Clique no IMAGEVIEW para abrir a câmera/galeria
        imagemRosto.setOnClickListener {
            mostrarDialogoEscolha()
        }
    }

    // --- 5. Funções de Ação (Abaixo do onCreate) ---

    private fun mostrarDialogoEscolha() {
        // Cria um pop-up de alerta
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escolha uma opção")
        builder.setItems(arrayOf("Tirar Foto", "Escolher da Galeria")) { dialog, which ->
            when (which) {
                0 -> abrirCamera() // "Tirar Foto"
                1 -> abrirGaleria() // "Escolher da Galeria"
            }
        }
        builder.show()
    }

    private fun abrirGaleria() {
        // Lança o contrato da galeria, pedindo qualquer tipo de imagem
        galleryLauncher.launch("image/*")
    }

    private fun abrirCamera() {
        // Cria um arquivo temporário para a câmera salvar a foto
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider", // Deve ser o mesmo "authorities" do Manifest
            file
        )
        // Lança o contrato da câmera, passando a Uri de onde salvar
        cameraLauncher.launch(imageUri)
    }
}