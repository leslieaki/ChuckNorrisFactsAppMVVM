package com.example.chucknorrisfactsappmvvm

import android.app.Application

class FactApp : Application() {

    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        viewModel = MainViewModel(
            FakeRepository()
        )
    }
}