package com.example.chucknorrisfactsappmvvm.data

import android.app.Application
import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.ProvideRealm
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.data.cloud.FactService
import com.example.chucknorrisfactsappmvvm.presentation.FactLiveDataWrapper
import com.example.chucknorrisfactsappmvvm.presentation.MainViewModel
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FactApp : Application() {

    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/jokes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val manageResources = ManageResources.Base(this)
        viewModel = MainViewModel(
            FactLiveDataWrapper.Base(),
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(FactService::class.java),
                    manageResources
                ),
                CacheDataSource.Base(object : ProvideRealm {
                    override fun provideRealm(): Realm = Realm.getDefaultInstance()
                }, manageResources)
            )
        )
    }
}