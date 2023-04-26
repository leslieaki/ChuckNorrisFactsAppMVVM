package com.example.chucknorrisfactsappmvvm.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.chucknorrisfactsappmvvm.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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
