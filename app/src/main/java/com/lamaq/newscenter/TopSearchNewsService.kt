package com.lamaq.newscenter

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TopSearchNewsService:TopNewsService {

    @GET("/v2/everything?apiKey=$API_KEY")
    fun getTopSearchNews(@Query("q") search: String, @Query("language") language: String, @Query("sortBy") sortBy: String, @Query("page") page: Int): Call<NewsList>

}
object TopSearchNewsApiService {
    val newsInstance : TopSearchNewsService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(TopSearchNewsService::class.java)
    }
}