package com.example.films

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

fun ImageView.loadImage(imageUrl: String) {
    val url = GlideUrl(BuildConfig.BASE_IMAGE_URL + imageUrl, LazyHeaders.Builder()
        .addHeader("api_key", BuildConfig.API_KEY)
        .build())
    Glide.with(this)
        .load(url)
        .into(this)
}