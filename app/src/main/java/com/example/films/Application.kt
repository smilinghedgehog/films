package com.example.films

import com.example.films.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class FilmsApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}