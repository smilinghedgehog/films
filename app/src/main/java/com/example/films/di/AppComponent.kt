package com.example.films.di

import android.app.Application
import com.example.films.FilmsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilder::class,
        ViewModelFactoryModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<FilmsApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}