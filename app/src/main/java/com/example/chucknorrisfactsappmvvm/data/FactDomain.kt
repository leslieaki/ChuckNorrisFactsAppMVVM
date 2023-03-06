package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCache
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface Fact {
    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
            type: String,
            setup: String,
            punchline: String,
            id: Int
        ): T
    }
}

data class FactDomain(
    private val type: String,
    private val setup: String,
    private val punchline: String,
    private val id: Int,
) : Fact {

    override fun <T> map(mapper: Fact.Mapper<T>): T = mapper.map(type, setup, punchline, id)
}

class ToCache : Fact.Mapper<FactCache> {
    override fun map(
        type: String,
        setup: String,
        punchline: String,
        id: Int
    ): FactCache {
        val factCache = FactCache()
        factCache.id = id
        factCache.text = setup
        factCache.punchline = punchline
        factCache.type = type
        return factCache
    }
}

class ToBaseUi : Fact.Mapper<FactUi> {
    override fun map(type: String, setup: String, punchline: String, id: Int): FactUi {
        return FactUi.Base(setup, punchline)
    }
}

class ToFavoriteUi : Fact.Mapper<FactUi> {
    override fun map(type: String, setup: String, punchline: String, id: Int): FactUi {
        return FactUi.Favorite(setup, punchline)
    }
}

class Change(
    private val cacheDataSource: CacheDataSource,
    private val toDomain: Fact.Mapper<FactDomain> = ToDomain()
) : Fact.Mapper<FactUi> {
    override fun map(type: String, setup: String, punchline: String, id: Int): FactUi {
        return cacheDataSource.addOrRemove(id, toDomain.map(type, setup, punchline, id))
    }
}

class ToDomain : Fact.Mapper<FactDomain> {
    override fun map(type: String, setup: String, punchline: String, id: Int): FactDomain {
        return FactDomain(type, setup, punchline, id)
    }

}