package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorrisfactsappmvvm.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository<FactUi, Error>,
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi(),
    private val dispatchersList: DispatchersList = DispatchersList.Base()
) : ViewModel() {

    private var factUiCallback: FactUiCallback = FactUiCallback.Empty()

    fun init(factUiCallback: FactUiCallback) {
        this.factUiCallback = factUiCallback
    }

    fun getFact() {
        viewModelScope.launch(dispatchersList.io()) {
            val result = repository.fetch()
            val ui = if (result.isSuccessful())
                result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            else
                FactUi.Failed(result.errorMessage())

            withContext(dispatchersList.ui()) {
                ui.show(factUiCallback)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        factUiCallback = FactUiCallback.Empty()
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeFactStatus() {
        viewModelScope.launch(dispatchersList.io()) {
            val factUi = repository.changeFactStatus()
            withContext(dispatchersList.ui()) {
                factUi.show(factUiCallback)
            }
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
}

interface DispatchersList {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

    class Base : DispatchersList {
        override fun io() = Dispatchers.IO

        override fun ui() = Dispatchers.Main
    }

}