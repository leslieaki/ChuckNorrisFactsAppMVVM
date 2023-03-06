package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.*
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import io.realm.Realm

interface CacheDataSource {

    fun addOrRemove(id: Int, fact: FactDomain): FactUi

    fun fetch(factCacheCallback: FactCacheCallback)

    class Base(
        private val realm: ProvideRealm,
        private val manageResources: ManageResources
    ) :
        CacheDataSource {

        private val error: Error by lazy {
            Error.NoFavoriteFact(manageResources)
        }

        override fun addOrRemove(id: Int, fact: FactDomain): FactUi {
            realm.provideRealm().let {
                val factCached = it.where(FactCache::class.java).equalTo("id", id).findFirst()
                if (factCached == null) {
                    it.executeTransaction { realm ->
                        val factCache = fact.map(ToCache())
                        realm.insert(factCache)
                    }
                    return fact.map(ToFavoriteUi())
                } else {
                    it.executeTransaction {
                        factCached.deleteFromRealm()
                    }
                    return fact.map(ToBaseUi())
                }
            }
        }

        override fun fetch(factCacheCallback: FactCacheCallback) {
            realm.provideRealm().let {
                val facts = it.where(FactCache::class.java).findAll()
                if (facts.isEmpty()) {
                    factCacheCallback.provideError(error)
                } else {
                    val factCached = facts.random()
                    factCacheCallback.provideFact(factCached.toFact())
                }
            }
        }
    }

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val error: Error by lazy {
            Error.NoFavoriteFact(manageResources)
        }

        private val map = mutableMapOf<Int, FactDomain>()


        override fun addOrRemove(id: Int, fact: FactDomain): FactUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                fact.map(ToBaseUi())
            } else {
                map[id] = fact
                fact.map(ToFavoriteUi())
            }
        }

        private var count = 0

        override fun fetch(factCacheCallback: FactCacheCallback) {
            if (map.isEmpty())
                factCacheCallback.provideError(error)
            else {
                if (++count == map.size) count = 0
                factCacheCallback.provideFact(
                    map.toList()[count].second
                )
            }
        }
    }
}


interface FactCacheCallback : ProvideError {
    fun provideFact(fact: FactDomain)
}


interface ProvideError {
    fun provideError(error: Error)
}

interface ProvideRealm {
    fun provideRealm(): Realm
}