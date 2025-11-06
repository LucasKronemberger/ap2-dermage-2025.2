package com.example.appdermageofc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AnalyzingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var statusButton: MaterialButton
    private lateinit var progressBarLinear: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analyzing)

        val root = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressBar = findViewById(R.id.progress_circle)
        progressBarLinear = findViewById(R.id.progressBarLinear)
        progressText = findViewById(R.id.textProgress)
        statusButton = findViewById(R.id.statusButton)

        val questions = intent.getParcelableArrayListExtra<QuizQuestion>("QUESTIONS_SO_FAR")
        val imageUri = intent.data // A Uri da imagem que veio da CapturaImagemActivity
        val etapaPulada = intent.getBooleanExtra("ETAPA_PULADA", false)

        if (questions != null) {
            startAnalysis(questions, imageUri, etapaPulada)
        } else {
            showError("Erro fatal: Dados do questionário não recebidos.")
        }
    }

    private fun startAnalysis(questions: ArrayList<QuizQuestion>, imageUri: Uri?, etapaPulada: Boolean) {


        val others = listOf(mapOf("user_id" to "usuario_teste_123", "timestamp" to System.currentTimeMillis().toString()))

        val skinProfile = SkinProfileRequest(questions = questions, others = others)

        val skinProfileJson = Gson().toJson(skinProfile)

        val skinDataPart: RequestBody = skinProfileJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        Log.d("MINHA_API_DEBUG", "Enviando 'skinData' (JSON): $skinProfileJson")


        val imageParts = mutableListOf<MultipartBody.Part>()
        if (imageUri != null && !etapaPulada) {
            val imagePart = createImagePart(imageUri, "images") // "images" é o nome do campo
            if (imagePart != null) {
                imageParts.add(imagePart)
            } else {
                showError("Erro ao processar o arquivo da imagem.")
                return
            }
        } else if (!etapaPulada) {

            showError("Nenhuma imagem selecionada.")
            return
        }


        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                progressBarLinear.isIndeterminate = true
                progressText.text = "Analisando sua pele..."

                val response = RetrofitClient.api.createAnalysis(skinDataPart, imageParts)

                if (response.isSuccessful) {
                    val analysisResponse = response.body()
                    if (analysisResponse != null) {

                        navigateToResult(analysisResponse)
                    } else {
                        showError("Resposta da análise veio vazia.")
                    }
                } else {
                    showError("Erro do servidor: ${response.code()} - ${response.message()}")
                    Log.e("MINHA_API_DEBUG", "Erro da API: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                showError("Falha na conexão: ${e.message}")
            }
        }
    }

    private fun createImagePart(imageUri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val fileBytes = inputStream?.readBytes()
            inputStream?.close()

            if (fileBytes != null) {
                val fileName = getFileName(imageUri)
                val mimeType = contentResolver.getType(imageUri) ?: "image/jpeg"

                val requestFile = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())

                MultipartBody.Part.createFormData(partName, fileName, requestFile)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val colIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (colIndex >= 0) {
                        result = cursor.getString(colIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        return result ?: "uploaded_image.jpg"
    }


    private fun navigateToResult(response: AnalysisResponse) {
        val intent = Intent(this, ResultadoActivity::class.java)
        intent.putExtra("ANALYSIS_RESULT", response)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        progressBarLinear.isIndeterminate = false
        progressBarLinear.progress = 0
        progressText.text = message

        statusButton.text = "Tentar Novamente"
        statusButton.isEnabled = true
        statusButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}