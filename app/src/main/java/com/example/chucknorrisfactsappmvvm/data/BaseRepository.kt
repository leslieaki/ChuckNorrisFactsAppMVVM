package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCallback
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val change: Fact.Mapper<FactUi> = Change(cacheDataSource),
    private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
    private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
) : Repository<FactUi, Error> {

    private var callback: ResultCallBack<FactUi, Error>? = null
    private var factTemporary: Fact? = null

    override fun fetch() {
        if (getFactFromCache) {
            cacheDataSource.fetch(object : FactCallback {
                override fun provideFact(fact: Fact) {
                    factTemporary = fact
                    callback?.provideSuccess(fact.map(toFavorite))
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })
        } else {
            cloudDataSource.fetch(object : FactCallback {
                override fun provideFact(fact: Fact) {
                    factTemporary = fact
                    callback?.provideSuccess(fact.map(toBaseUi))
                }

                override fun provideError(error: Error) {
                    factTemporary = null
                    callback?.provideError(error)
                }
            })
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