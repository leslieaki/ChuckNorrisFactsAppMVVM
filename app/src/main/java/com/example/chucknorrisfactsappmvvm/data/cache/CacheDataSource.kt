package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.*
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources

interface CacheDataSource : DataSource {

    suspend fun addOrRemove(id: String, fact: Fact): FactUi

    class Base(
        private val realm: ProvideRealm,
        manageResources: ManageResources,
        private val error: Error = Error.NoFavoriteFact(manageResources),
        private val mapper: Fact.Mapper<FactCache> = ToCache(),
        private val toFavorite: Fact.Mapper<FactUi> = ToFavoriteUi(),
        private val toBaseUi: Fact.Mapper<FactUi> = ToBaseUi()
    ) : CacheDataSource {

        override suspend fun addOrRemove(id: String, fact: Fact): FactUi {
            realm.provideRealm().use { realm ->
                val factCached = realm.where(FactCache::class.java).equalTo("id", id).findFirst()
                return if (factCached == null) {
                    val factCache = fact.map(mapper)
                    realm.executeTransaction {
                        it.insert(factCache)
                    }
                    fact.map(toFavorite)
                } else {
                    realm.executeTransaction {
                        factCached.deleteFromRealm()
                    }
                    fact.map(toBaseUi)
                }
            }
        }

        override suspend fun fetch(): FactResult {
            realm.provideRealm().use { realm ->
                val facts = realm.where(FactCache::class.java).findAll()
                return if (facts.isEmpty())
                    FactResult.Failure(error)
                else FactResult.Success(realm.copyFromRealm(facts.random()), true)
            }
        }
    }
}