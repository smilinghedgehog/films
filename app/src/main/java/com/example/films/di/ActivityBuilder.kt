package com.example.films.di

import com.example.films.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class, ViewModelModule::class])
    abstract fun mainActivity(): MainActivity
}