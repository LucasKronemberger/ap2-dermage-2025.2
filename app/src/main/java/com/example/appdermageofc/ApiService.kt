package com.example.appdermageofc

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/analyze") // end analise
    suspend fun createAnalysis(

        // form
        @Part("skinData") skinData: RequestBody,

        @Part images: List<MultipartBody.Part>

    ): Response<AnalysisResponse>
}