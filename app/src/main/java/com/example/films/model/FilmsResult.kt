package com.example.films.model

import com.google.gson.annotations.SerializedName

data class FilmsResult(
    @SerializedName("page") val page: Int?,
    @SerializedName("results") val results: ArrayList<FilmModel>?
)