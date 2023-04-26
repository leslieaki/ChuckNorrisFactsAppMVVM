package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

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