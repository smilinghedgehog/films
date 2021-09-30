package com.example.films

import android.content.Context
import androidx.core.content.edit

class FavouritesHelper(private val context: Context) {

    fun getFavouritesSet(): Set<String> =
        context.getSharedPreferences(FILMS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            ?.getStringSet(FAVOURITES_SET, hashSetOf()) ?: HashSet()

    fun setFavouritesSet(favourites: HashSet<String>) {
        context.getSharedPreferences(FILMS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            ?.edit { putStringSet(FAVOURITES_SET, favourites) }
    }

    companion object {
        private const val FILMS_SHARED_PREFERENCES = "FILMS_SHARED_PREFERENCES"
        private const val FAVOURITES_SET = "FAVOURITES_SET"
    }
}