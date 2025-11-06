package com.example.appdermageofc

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/analyze") // <-- CORREÇÃO 1: Adicionada a barra "/"
    suspend fun createAnalysis(
        // CORREÇÃO 2: A API espera "multipart/form-data"

        // As perguntas e outros dados vão como um campo de formulário
        @Part("skinData") skinData: RequestBody,

        // Os arquivos de imagem vão como outro campo
        // O nome "images" DEVE ser o mesmo do Python
        @Part images: List<MultipartBody.Part>

    ): Response<AnalysisResponse> // A resposta continua a mesma
}