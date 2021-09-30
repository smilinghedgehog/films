package com.example.films.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.films.FavouritesHelper
import com.example.films.R
import com.example.films.databinding.FilmItemBinding
import com.example.films.loadImage
import com.example.films.model.FilmModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class FilmsAdapter: RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private val items = ArrayList<FilmModel>()
    private var favourites = HashSet<String>()

    private var helper: FavouritesHelper? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        helper = FavouritesHelper(recyclerView.context)
        favourites = HashSet(helper?.getFavouritesSet())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: ArrayList<FilmModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun saveData() {
        helper?.setFavouritesSet(favourites)
    }

    inner class ViewHolder(private val binding: FilmItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(filmModel: FilmModel) {
            with (binding) {
                title.text = filmModel.title
                overview.text = filmModel.overview

                likeBtn.setImageResource(
                    if (favourites.contains(filmModel.id?.toString())) R.drawable.ic_heart_fill
                    else R.drawable.ic_heart
                )
                likeBtn.setOnClickListener {
                    if (favourites.contains(filmModel.id?.toString())) {
                        favourites.remove(filmModel.id?.toString())
                        likeBtn.setImageResource(R.drawable.ic_heart)
                    } else {
                        favourites.add(filmModel.id?.toString()!!)
                        likeBtn.setImageResource(R.drawable.ic_heart_fill)
                    }
                }

                if (filmModel.releaseDate != null) {
                    val initialFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
                    val finalFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
                    val date = initialFormat.parse(filmModel.releaseDate)
                    if (date != null) releaseDate.text = finalFormat.format(date)
                }

                if (filmModel.posterPath != null) {
                    poster.loadImage(filmModel.posterPath)
                }
            }
        }
    }
}