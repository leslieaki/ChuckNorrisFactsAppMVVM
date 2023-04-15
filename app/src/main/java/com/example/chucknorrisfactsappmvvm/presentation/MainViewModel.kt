package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorrisfactsappmvvm.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val factLiveDataWrapper: FactLiveDataWrapper,
    private val repository: Repository<FactUi, Error>,
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi(),
    dispatchersList: DispatchersList = DispatchersList.Base(),
) : BaseViewModel(dispatchersList = dispatchersList), Observe<FactUi> {

    private val blockUi: suspend (FactUi) -> Unit = {
        factLiveDataWrapper.map(it)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<FactUi>) =
        factLiveDataWrapper.observe(owner, observer)

    fun getFact() {
        handle({
            val result = repository.fetch()
            if (result.isSuccessful())
                result.map(if (result.toFavorite()) toFavorite else toBaseUi)
            else
                FactUi.Failed(result.errorMessage())
        }, blockUi)
    }

    fun chooseFavorite(favorites: Boolean) {
        repository.chooseFavorites(favorites)
    }

    fun changeFactStatus() {
        handle({ repository.changeFactStatus() }, blockUi)
    }
}

interface FactUiCallback {

    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)
}

interface DispatchersList {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

    class Base : DispatchersList {
        override fun io() = Dispatchers.IO

        override fun ui() = Dispatchers.Main
    }
}

abstract class BaseViewModel(
    private val dispatchersList: DispatchersList
) : ViewModel() {
    fun <T> handle(
        blockIo: suspend () -> T,
        blockUi: suspend (T) -> Unit
    ) = viewModelScope.launch(dispatchersList.io()) {
        val result = blockIo.invoke()
        withContext(dispatchersList.ui()) {
            blockUi.invoke(result)
        }
    }
}
