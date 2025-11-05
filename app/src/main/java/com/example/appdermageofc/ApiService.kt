package com.example.appdermageofc

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // "analise" é o caminho do endpoint. Se for diferente, troque aqui.
    @POST("analise")
    suspend fun createAnalysis(
        @Body request: CreateAnalysisRequest
    ): Response<AnalysisResponse> // Retorna a Resposta da API
}