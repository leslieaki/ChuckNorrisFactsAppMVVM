package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCacheCallback
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloud
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloudCallback
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) : Repository<FactUi, Error> {

    private var callback: ResultCallBack<FactUi, Error>? = null

    private var factCloudCached: FactCloud? = null

    override fun fetch() {
        if (getFactFromCache) {
            cacheDataSource.fetch(object : FactCacheCallback {
                override fun provideFact(fact: FactCloud) {
                    factCloudCached = fact
                    callback?.provideSuccess(fact.toFavoriteUi())
                }

                override fun provideError(error: Error) {
                    callback?.provideError(error)
                }

            })
        } else {
            cloudDataSource.fetch(object : FactCloudCallback {
                override fun provideFactCloud(factCloud: FactCloud) {
                    factCloudCached = factCloud
                    callback?.provideSuccess(factCloud.toUi())
                }

                override fun provideError(error: Error) {
                    factCloudCached = null
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
        factCloudCached?.let {
            resultCallBack.provideSuccess(it.change(cacheDataSource))
        }
    }

    private var getFactFromCache = false

    override fun chooseFavorites(favorites: Boolean) {
        getFactFromCache = favorites
    }
}