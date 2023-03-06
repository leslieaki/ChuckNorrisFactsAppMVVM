package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCacheCallback
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloudCallback
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) : Repository<FactUi, Error> {

    private var callback: ResultCallBack<FactUi, Error>? = null

    private var factTemporary: FactDomain? = null

    override fun fetch() {
        if (getFactFromCache) {
            cacheDataSource.fetch(object : FactCacheCallback {
                override fun provideFact(fact: FactDomain) {
                    factTemporary = fact
                    callback?.provideSuccess(fact.map(ToFavoriteUi()))
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })
        } else {
            cloudDataSource.fetch(object : FactCloudCallback {
                override fun provideFact(fact: FactDomain) {
                    factTemporary = fact
                    callback?.provideSuccess(fact.map(ToBaseUi()))
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
            resultCallBack.provideSuccess(it.map(Change(cacheDataSource)))
        }
    }

    private var getFactFromCache = false

    override fun chooseFavorites(favorites: Boolean) {
        getFactFromCache = favorites
    }
}