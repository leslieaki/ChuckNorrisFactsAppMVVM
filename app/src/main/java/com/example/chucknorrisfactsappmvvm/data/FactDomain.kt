package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCache
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface Fact {
    suspend fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        suspend fun map(
            createdDate: String,
            iconUrl: String,
            id: String,
            updateDate: String,
            url: String,
            value: String
        ): T
    }
}

data class FactDomain(
    private val createdDate: String,
    private val iconUrl: String,
    private val id: String,
    private val updateDate: String,
    private val url: String,
    private val value: String
) : Fact {

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
        mapper.map(createdDate, iconUrl, id, updateDate, url, value)
}

class ToCache : Fact.Mapper<FactCache> {

    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactCache {
        val factCache = FactCache()
        factCache.createdDate = createdDate
        factCache.iconUrl = iconUrl
        factCache.id = id
        factCache.updateDate = updateDate
        factCache.url = url
        factCache.value = value
        return factCache
    }
}

class ToBaseUi : Fact.Mapper<FactUi> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactUi {
        return FactUi.Base(value)
    }
}

class ToFavoriteUi : Fact.Mapper<FactUi> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactUi {
        return FactUi.Favorite(value)
    }
}

class Change(
    private val cacheDataSource: CacheDataSource,
    private val toDomain: Fact.Mapper<FactDomain> = ToDomain()
) : Fact.Mapper<FactUi> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactUi {
        return cacheDataSource.addOrRemove(
            id, toDomain.map(createdDate, iconUrl, id, updateDate, url, value)
        )
    }
}

class ToDomain : Fact.Mapper<FactDomain> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactDomain {
        return FactDomain(createdDate, iconUrl, id, updateDate, url, value)
    }
}