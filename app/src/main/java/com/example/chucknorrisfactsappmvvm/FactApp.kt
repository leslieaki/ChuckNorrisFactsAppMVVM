package com.example.chucknorrisfactsappmvvm

import android.app.Application

class FactApp : Application() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate() {
        super.onCreate()
        viewModel = MainViewModel()
    }
}