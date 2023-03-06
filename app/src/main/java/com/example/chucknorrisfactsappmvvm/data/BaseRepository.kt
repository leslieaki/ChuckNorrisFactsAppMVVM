package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCallback
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val change: Fact.Mapper<FactUi> = Change(cacheDataSource),
    toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
) : Repository<FactUi, Error> {

    private var callback: ResultCallBack<FactUi, Error>? = null
    private var factTemporary: Fact? = null
    private val factCacheCallback = BaseFactCallback(toFavorite)
    private val cloudCallback = BaseFactCallback(toBaseUi)

    override fun fetch() = if (getFactFromCache)
        cacheDataSource.fetch(factCacheCallback)
    else
        cloudDataSource.fetch(cloudCallback)


    private inner class BaseFactCallback(
        private val mapper: Fact.Mapper<FactUi>
    ) : FactCallback {
        override fun provideFact(fact: Fact) {
            factTemporary = fact
            callback?.provideSuccess(fact.map(mapper))
        }

        override fun provideError(error: Error) {
            factTemporary = null
            callback?.provideError(error)
        }
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<FactUi, Error>) {
        this.callback = callback
    }

    override fun changeFactStatus(resultCallBack: ResultCallBack<FactUi, Error>) {
        factTemporary?.let {
            resultCallBack.provideSuccess(it.map(change))
        }
    }

    private var getFactFromCache = false

    override fun chooseFavorites(favorites: Boolean) {
        getFactFromCache = favorites
    }
}