package com.lamaq.newscenter

data class NewsList(
    val articles: List<Articles>,
    val totalResults: Int
)
