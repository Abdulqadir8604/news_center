package com.lamaq.newscenter

data class NewsList(
    val articles: List<Articles>,
    val status: String,
    val totalResults: Int
)
