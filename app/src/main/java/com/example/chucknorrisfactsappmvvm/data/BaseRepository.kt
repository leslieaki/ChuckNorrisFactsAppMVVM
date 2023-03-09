package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactResult
import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
    private val change: Fact.Mapper<FactUi> = Change(cacheDataSource)
) : Repository<FactUi, Error> {

    private var factTemporary: Fact? = null

    override suspend fun fetch(): FactResult {
        val factResult = if (getFactFromCache)
            cacheDataSource.fetch()
        else
            cloudDataSource.fetch()
        factTemporary = if (factResult.isSuccessful()) {
            factResult.map(ToDomain())
        } else null
        return factResult
    }

    override suspend fun changeFactStatus(): FactUi {
        return factTemporary!!.map(change)
    }

    private var getFactFromCache = false

    override fun chooseFavorites(favorites: Boolean) {
        getFactFromCache = favorites
    }
}