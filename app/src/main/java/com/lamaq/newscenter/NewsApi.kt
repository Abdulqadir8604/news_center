package com.lamaq.newscenter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org/v2/"
const val API_KEY =  "bb35820342c54275bea094772d3b2c78"

interface NewsApi {

    @GET("/v2/top-headlines?apiKey=$API_KEY")
    fun getNews(@Query("country") country: String, @Query("page") page:Int): Call<Articles>

}