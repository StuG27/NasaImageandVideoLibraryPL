package com.volynkin.nasaimageandvideolibrary.data

data class NASAItem(
    val id: String,
    val jsonLink: String,
    val preview: String,
    val description: String,
    val title: String,
    val date: String,
    val type: String,
    val keywords: String
)