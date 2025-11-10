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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import java.io.File

class CapturaImagemActivity : AppCompatActivity() {

    // Variável para guardar o caminho (Uri) da foto tirada pela câmera
    private var imageUri: Uri? = null

    // 1. Variavel para guardar a corrente de perguntas
    private var questions: ArrayList<QuizQuestion>? = null

    // --- 1. Lançador da Galeria ---
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(it)
        }
    }

    // --- 2. Lançador da Câmera ---
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- CÓDIGO DA TELA CHEIA ---
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

        // recebe as perguntas anteriores
        questions = intent.getParcelableArrayListExtra<QuizQuestion>("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList() // Garante que não seja nula
        }


        val botaoVoltar = findViewById<ImageButton>(R.id.btnBack)
        val botaoConfirmar = findViewById<MaterialButton>(R.id.btnConfirmar)
        val botaoPular = findViewById<TextView>(R.id.skipText)
        val imagemRosto = findViewById<ImageView>(R.id.fotoRosto)

        botaoVoltar.setOnClickListener {
            finish()
        }


        botaoPular.setOnClickListener {
            val intent = Intent(this, AnalyzingActivity::class.java)

            intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)
            intent.putExtra("ETAPA_PULADA", true) // Avisa que não tem foto

            startActivity(intent)
        }


        botaoConfirmar.setOnClickListener {
            if (imageUri != null) {
                val intent = Intent(this, AnalyzingActivity::class.java)

                // 👈 4. PASSA A LISTA DE PERGUNTAS E A FOTO ADIANTE
                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)
                intent.data = imageUri // Adiciona a Uri da imagem

                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, envie uma foto", Toast.LENGTH_SHORT).show()
            }
        }

        imagemRosto.setOnClickListener {
            mostrarDialogoEscolha()
        }
    }

    private fun mostrarDialogoEscolha() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escolha uma opção")
        builder.setItems(arrayOf("Tirar Foto", "Escolher da Galeria")) { dialog, which ->
            when (which) {
                0 -> abrirCamera()
                1 -> abrirGaleria()
            }
        }
        builder.show()
    }

    private fun abrirGaleria() {
        galleryLauncher.launch("image/*")
    }

    private fun abrirCamera() {
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            file
        )
        cameraLauncher.launch(imageUri)
    }
}