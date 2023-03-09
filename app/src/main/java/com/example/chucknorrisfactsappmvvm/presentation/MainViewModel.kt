package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorrisfactsappmvvm.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: Repository<FactUi, Error>,
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
) : ViewModel() {
    private var factUiCallback: FactUiCallback = FactUiCallback.Empty()

    fun init(factUiCallback: FactUiCallback) {
        this.factUiCallback = factUiCallback
    }

    fun getFact() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.fetch()
            val ui = if (result.isSuccessful())
                result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            else
                FactUi.Failed(result.errorMessage())

            withContext(Dispatchers.Main) {
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
        viewModelScope.launch(Dispatchers.IO) {
            val factUi = repository.changeFactStatus()
            withContext(Dispatchers.Main) {
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