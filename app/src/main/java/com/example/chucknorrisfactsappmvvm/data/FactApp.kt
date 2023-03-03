package com.example.chucknorrisfactsappmvvm.data

import android.app.Application
import com.example.chucknorrisfactsappmvvm.presentation.MainViewModel
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
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
            FakeRepository(
                ManageResources.Base(this)
            )
//          BaseRepository (
//          retrofit.create(FactService::class.java),
//          ManageResources.Base(this)
        )
    }
}