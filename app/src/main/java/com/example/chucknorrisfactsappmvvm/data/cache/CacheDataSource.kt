package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.*
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import io.realm.Realm

interface CacheDataSource : DataSource {

    fun addOrRemove(id: Int, fact: Fact): FactUi

    class Base(
        private val realm: ProvideRealm,
        manageResources: ManageResources,
        private val error: Error = Error.NoFavoriteFact(manageResources),
        private val mapper: Fact.Mapper<FactCache> = ToCache(),
        private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
        private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
    ) : CacheDataSource {

        override fun addOrRemove(id: Int, fact: Fact): FactUi {
            realm.provideRealm().let {
                val factCached = it.where(FactCache::class.java).equalTo("id", id).findFirst()
                if (factCached == null) {
                    it.executeTransaction { realm ->
                        val factCache = fact.map(mapper)
                        realm.insert(factCache)
                    }
                    return fact.map(toFavorite)
                } else {
                    it.executeTransaction {
                        factCached.deleteFromRealm()
                    }
                    return fact.map(toBaseUi)
                }
            }
        }

        override fun fetch(factCallback: FactCallback) {
            realm.provideRealm().let {
                val facts = it.where(FactCache::class.java).findAll()
                if (facts.isEmpty()) {
                    factCallback.provideError(error)
                } else {
                    val factCached = facts.random()
                    factCallback.provideFact(it.copyFromRealm(factCached))
                }
            }
        }
    }

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val error: Error by lazy {
            Error.NoFavoriteFact(manageResources)
        }

        private val map = mutableMapOf<Int, Fact>()


        override fun addOrRemove(id: Int, fact: Fact): FactUi {
            return if (map.containsKey(id)) {
                map.remove(id)
                fact.map(ToBaseUi())
            } else {
                map[id] = fact
                fact.map(ToFavoriteUi())
            }
        }

        private var count = 0

        override fun fetch(factCallback: FactCallback) {
            if (map.isEmpty())
                factCallback.provideError(error)
            else {
                if (++count == map.size) count = 0
                factCallback.provideFact(
                    map.toList()[count].second
                )
            }
        }
    }
}

interface DataSource {
    fun fetch(factCallback: FactCallback)
}

interface FactCallback : ProvideError {
    fun provideFact(fact: Fact)
}

interface ProvideError {
    fun provideError(error: Error)
}

interface ProvideRealm {
    fun provideRealm(): Realm
}