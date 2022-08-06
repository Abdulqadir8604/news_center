package com.lamaq.newscenter

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TopPerCategoryNewsService:TopNewsService {

    @GET("/v2/top-headlines?apiKey=$API_KEY")
    fun getTopCatNews(@Query("country") country: String, @Query("category") category: String, @Query("page") page: Int, @Query("pageSize") pageSize:Int): Call<NewsList>

}
object TopPerCatNewsApiService {
    val newsInstance : TopPerCategoryNewsService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(TopPerCategoryNewsService::class.java)
    }
}