package com.example.films.net

import android.util.Log
import com.example.films.model.FilmsResult
import retrofit2.Retrofit

class FilmsRepository (private val retrofit: Retrofit): FilmsApi {

    private val service = retrofit.create(FilmsApi::class.java)

    override suspend fun getFilms(apiKey: String, language: String): FilmsResult? {
        return try {
            service.getFilms(apiKey, language)
        } catch (e: Exception) {
            Log.e(FilmsRepository::class.simpleName, e.message, e)
            null
        }
    }

    override suspend fun searchFilm(apiKey: String, query: String, language: String): FilmsResult? {
        return try {
            service.searchFilm(apiKey, query, language)
        } catch (e: Exception) {
            Log.e(FilmsRepository::class.simpleName, e.message, e)
            null
        }
    }
}