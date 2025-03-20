package br.edu.ifpb.sleepwell.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://vercel-db-json-two.vercel.app/"

    val instance: DicaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DicaApiService::class.java)
    }
}
