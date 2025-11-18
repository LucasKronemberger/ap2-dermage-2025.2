package com.example.appdermageofc

import android.Manifest // 👈 IMPORT ADICIONADO
import android.content.Intent
import android.content.pm.PackageManager // 👈 IMPORT ADICIONADO
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // 👈 IMPORT ADICIONADO
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // 👈 IMPORT ADICIONADO
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.io.File

class CapturaImagemActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var questions: ArrayList<QuizQuestion>? = null

    // --- 1. Lançador da Galeria (Sem mudanças) ---
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(it)
        }
    }

    // --- 2. Lançador da Câmera (Sem mudanças) ---
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            findViewById<ImageView>(R.id.fotoRosto).setImageURI(imageUri)
        }
    }

    // --- 3. NOVO Lançador de PERMISSÃO da Câmera ---
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permissão foi DADA. Agora sim, podemos abrir a câmera.
            abrirCamera()
        } else {
            // Permissão foi NEGADA. Avise o usuário.
            Toast.makeText(this, "Permissão da câmera negada.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_captura_imagem)

        // --- Seu código de EdgeToEdge ---
        val root = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Receber a "corrente" de perguntas ---
        questions = intent.getParcelableArrayListExtra<QuizQuestion>("QUESTIONS_SO_FAR")
        if (questions == null) {
            questions = ArrayList()
        }

        // --- Encontrar os Botões ---
        val botaoVoltar = findViewById<ImageButton>(R.id.btnBack)
        val botaoConfirmar = findViewById<MaterialButton>(R.id.btnConfirmar)
        val imagemRosto = findViewById<ImageView>(R.id.fotoRosto)

        // --- Configurar Cliques ---
        botaoVoltar.setOnClickListener { finish() }


        botaoConfirmar.setOnClickListener {
            if (imageUri != null) {
                val intent = Intent(this, AnalyzingActivity::class.java)
                intent.putParcelableArrayListExtra("QUESTIONS_SO_FAR", questions)
                intent.data = imageUri
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, envie uma foto", Toast.LENGTH_SHORT).show()
            }
        }

        imagemRosto.setOnClickListener {
            mostrarDialogoEscolha()
        }
    }

    // --- 4. Funções de Ação (Abaixo do onCreate) ---

    private fun mostrarDialogoEscolha() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Escolha uma opção")
        builder.setItems(arrayOf("Tirar Foto", "Escolher da Galeria")) { dialog, which ->
            when (which) {
                0 -> checkCameraPermissionAndLaunch() // <-- MUDANÇA AQUI
                1 -> abrirGaleria()
            }
        }
        builder.show()
    }

    // --- 5. NOVA Função de Checagem de Permissão ---
    private fun checkCameraPermissionAndLaunch() {
        when {
            // 1. Verifica se a permissão JÁ FOI DADA
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permissão já existe, pode abrir a câmera
                abrirCamera()
            }
            // 2. (Opcional) Mostra uma explicação se o usuário já negou antes
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Mostre um pop-up explicando "Por que precisamos da câmera"
                // Por simplicidade, vamos apenas pedir de novo:
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            // 3. Pede a permissão pela primeira vez
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun abrirGaleria() {
        galleryLauncher.launch("image/*")
    }

    private fun abrirCamera() {
        // (Sem mudanças aqui, esta função está correta)
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            file
        )
        cameraLauncher.launch(imageUri)
    }
}