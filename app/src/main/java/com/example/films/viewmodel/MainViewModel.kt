package com.example.films.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.films.BuildConfig
import com.example.films.model.FilmModel
import com.example.films.net.FilmsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: FilmsRepository
): ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: MutableLiveData<State> = _state

    init {
        getFilmsList()
    }

    fun onSearchTextChange(searchQuery: String?) {
        if (searchQuery.isNullOrEmpty()) getFilmsList()
        else searchFilm(searchQuery)
    }

    fun onRefresh(searchQuery: CharSequence?) {
        if (searchQuery.isNullOrEmpty()) getFilmsList()
        else searchFilm(searchQuery.toString())
    }

    private fun getFilmsList() {
        CoroutineScope(Dispatchers.IO).launch {
            _state.postValue(State.Loading(isIndicatorLoadingType()))

            val films = repository.getFilms(BuildConfig.API_KEY, "ru-RU")
            if (films != null) {
                if (films.results?.isEmpty() != false) _state.postValue(State.Error)
                else _state.postValue(State.Success(films.results))
            } else _state.postValue(State.Error)
        }
    }

    private fun searchFilm(searchQuery: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _state.postValue(State.Loading(isIndicatorLoadingType()))

            val film = repository.searchFilm(BuildConfig.API_KEY, searchQuery,"ru-RU")
            if (film != null) {
                if (film.results?.isEmpty() != false)
                    _state.postValue(State.SearchFailed(searchQuery))
                else _state.postValue(State.Success(film.results))
            } else _state.postValue(State.Error)
        }
    }

    private fun isIndicatorLoadingType(): Boolean {
        return (state.value as? State.Success)?.result?.isNotEmpty() ?: false
    }

    sealed class State {
        data class Loading(val indicatorLoading: Boolean): State()
        data class SearchFailed(val searchQuery: String): State()
        object Error: State()
        data class Success(val result: ArrayList<FilmModel>): State()
    }
}