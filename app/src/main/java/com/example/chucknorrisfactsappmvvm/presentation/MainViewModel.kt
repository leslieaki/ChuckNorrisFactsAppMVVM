package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.Repository
import com.example.chucknorrisfactsappmvvm.data.ResultCallBack

class MainViewModel(private val repository: Repository<FactUi, Error>) {

    private var factUiCallback: FactUiCallback = FactUiCallback.Empty()

    private val resultCallBack = object : ResultCallBack<FactUi, Error> {

        override fun provideSuccess(data: FactUi) = data.show(factUiCallback)

        override fun provideError(error: Error) = FactUi.Failed(error.message()).show(factUiCallback)
    }

    fun init(factUiCallback: FactUiCallback) {
        this.factUiCallback = factUiCallback
        repository.init(resultCallBack)
    }

    fun getFact() {
        repository.fetch()
    }

    fun clear() {
        factUiCallback = FactUiCallback.Empty()
        repository.clear()
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeFactStatus() {
        repository.changeFactStatus(resultCallBack)
    }
}

interface FactUiCallback {

    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)

    class Empty : FactUiCallback {

        override fun provideText(text: String) = Unit

        override fun provideIconResId(iconResId: Int) = Unit
    }
}