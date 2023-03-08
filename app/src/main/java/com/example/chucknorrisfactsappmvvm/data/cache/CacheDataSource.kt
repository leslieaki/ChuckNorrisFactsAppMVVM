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
            val realm = realm.provideRealm()
            val factCached = realm.where(FactCache::class.java).equalTo("id", id).findFirst()
            return if (factCached == null) {
                realm.executeTransaction {
                    val factCache = fact.map(mapper)
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

        override fun fetch(): FactResult {
            val realm = realm.provideRealm()
            val facts = realm.where(FactCache::class.java).findAll()
            return if (facts.isEmpty())
                FactResult.Failure(error)
            else FactResult.Success(realm.copyFromRealm(facts.random()), true)
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

    override fun fetch(): FactResult {
        return if (map.isEmpty())
            FactResult.Failure(error)
        else {
            if (++count == map.size) count = 0
            FactResult.Success(map.toList()[count].second, true)
        }
    }
}

interface DataSource {
    fun fetch(): FactResult
}

interface FactResult : Fact {

    fun toFavorite(): Boolean

    fun isSuccessful(): Boolean

    fun errorMessage(): String

    class Success(private val fact: Fact, private val toFavorite: Boolean) : FactResult {

        override fun toFavorite(): Boolean = toFavorite

        override fun isSuccessful(): Boolean = true

        override fun errorMessage(): String = ""

        override fun <T> map(mapper: Fact.Mapper<T>): T {
            return fact.map(mapper)
        }
    }

    class Failure(private val error: Error) : FactResult {

        override fun toFavorite(): Boolean = false

        override fun isSuccessful(): Boolean = false

        override fun errorMessage(): String = error.message()

        override fun <T> map(mapper: Fact.Mapper<T>): T = throw java.lang.IllegalStateException()
    }
}

interface ProvideRealm {
    fun provideRealm(): Realm
}