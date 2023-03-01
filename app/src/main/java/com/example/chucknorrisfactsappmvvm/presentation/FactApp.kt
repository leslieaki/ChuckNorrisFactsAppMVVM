package com.example.chucknorrisfactsappmvvm.presentation

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FactApp : Application() {

    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/jokes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModel = MainViewModel(
            BaseRepository(
                retrofit.create(FactService::class.java),
                ManageResources.Base(this)
            )
        )
    }
}