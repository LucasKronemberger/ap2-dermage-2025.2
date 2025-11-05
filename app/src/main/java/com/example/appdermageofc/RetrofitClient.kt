package com.example.appdermageofc

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ❗️❗️❗️ MUITO IMPORTANTE ❗️❗️❗️
    // Substitua pela URL base real da sua API
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Cria um "Logger" para vermos as chamadas de rede no Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Adiciona o Logger ao cliente HTTP
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Cria a instância do Retrofit
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}