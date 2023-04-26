package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.FactCache

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