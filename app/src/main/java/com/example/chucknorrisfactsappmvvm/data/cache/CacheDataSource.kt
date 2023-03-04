package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloud
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import io.realm.Realm

interface CacheDataSource {

    fun addOrRemove(id: String, fact: FactCloud): FactUi

    fun fetch(factCacheCallback: FactCacheCallback)

    class Base(
        private val realm: Realm,
        private val manageResources: ManageResources
    ) :
        CacheDataSource {

        private val error: Error by lazy {
            Error.NoFavoriteFact(manageResources)
        }

        override fun addOrRemove(id: String, fact: FactCloud): FactUi {
            realm.use {
                val factCached = it.where(FactCache::class.java).equalTo("id", id).findFirst()
                if (factCached == null) {
                    it.executeTransactionAsync { realm ->
                        val factCache = fact.toCache()
                        realm.insert(factCache)
                    }
                    return fact.toFavoriteUi()
                } else {
                    it.executeTransactionAsync { realm ->
                        factCached.deleteFromRealm()
                    }
                    return fact.toUi()
                }
            }
        }

        override fun fetch(factCacheCallback: FactCacheCallback) {
            realm.use {
                val facts = it.where(FactCache::class.java).findAll()
                if (facts.isEmpty()) {
                    factCacheCallback.provideError(error)
                } else {
                    val factCached = facts.random()
                    factCacheCallback.provideFact(
                        FactCloud(
                            factCached.categories,
                            factCached.createdAt,
                            factCached.iconUrl,
                            factCached.id,
                            factCached.updatedAt,
                            factCached.url,
                            factCached.value
                        )
                    )
                }
            }
        }

    }

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val error: Error by lazy {
            Error.NoFavoriteFact(manageResources)
        }

        private val map = mutableMapOf<String, FactCloud>()


        override fun addOrRemove(id: String, fact: FactCloud): FactUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                fact.toUi()
            } else {
                map[id] = fact
                fact.toFavoriteUi()
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
    fun provideFact(fact: FactCloud)
}


interface ProvideError {
    fun provideError(error: Error)
}
