package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.chucknorrisfactsappmvvm.data.*

class MainViewModel(
    private val repository: Repository<FactUi, Error>,
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
) {
    private var factUiCallback: FactUiCallback = FactUiCallback.Empty()

    fun init(factUiCallback: FactUiCallback) {
        this.factUiCallback = factUiCallback
    }

    fun getFact() = Thread {
        val result = repository.fetch()
        if (result.isSuccessful())
            result.map(if (result.toFavorite()) toFavorite else toBaseUi).show(factUiCallback)
        else
            FactUi.Failed(result.errorMessage()).show(factUiCallback)
    }.start()

    fun clear() {
        factUiCallback = FactUiCallback.Empty()
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeFactStatus() = Thread {
        val factUi = repository.changeFactStatus()
        factUi.show(factUiCallback)
    }.start()
}

interface FactUiCallback {

    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)

    class Empty : FactUiCallback {

        override fun provideText(text: String) = Unit

        override fun provideIconResId(iconResId: Int) = Unit
    }
}