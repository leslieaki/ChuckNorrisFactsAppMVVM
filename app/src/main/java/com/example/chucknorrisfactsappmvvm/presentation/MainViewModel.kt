package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorrisfactsappmvvm.data.*
import kotlinx.coroutines.*

class MainViewModel(
    private val repository: Repository<FactUi, Error>,
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi(),
    private val handleUi: HandleUi = HandleUi.Base(DispatchersList.Base())
) : ViewModel() {

    private var factUiCallback: FactUiCallback = FactUiCallback.Empty()

    fun init(factUiCallback: FactUiCallback) {
        this.factUiCallback = factUiCallback
    }

    fun getFact() = handleUi.handle(viewModelScope, factUiCallback) {
        val result = repository.fetch()
        if (result.isSuccessful())
            result.map(if (result.toFavorite()) toFavorite else toBaseUi)
        else
            FactUi.Failed(result.errorMessage())
    }

    override fun onCleared() {
        super.onCleared()
        factUiCallback = FactUiCallback.Empty()
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeFactStatus() = handleUi.handle(viewModelScope, factUiCallback) {
        repository.changeFactStatus()
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

interface HandleUi {

    fun handle(
        coroutineScope: CoroutineScope,
        factUiCallback: MainViewModel.FactUiCallback,
        block: suspend () -> FactUi
    )

    class Base(private val dispatchersList: DispatchersList) : HandleUi {
        override fun handle(
            coroutineScope: CoroutineScope,
            factUiCallback: MainViewModel.FactUiCallback,
            block: suspend () -> FactUi
        ) {
            coroutineScope.launch(dispatchersList.io()) {
                val factUi = block.invoke()
                withContext(dispatchersList.ui()) {
                    factUi.show(factUiCallback)
                }
            }
        }
    }
}