package com.example.films.net

import com.example.films.model.FilmsResult
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsApi {
    @GET("/3/discover/movie")
    suspend fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): FilmsResult?

    @GET("/3/search/movie")
    suspend fun searchFilm(
            @Query("api_key") apiKey: String,
            @Query("query") query: String,
            @Query("language") language: String
    ): FilmsResult?
}