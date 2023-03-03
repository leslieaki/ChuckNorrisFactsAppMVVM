package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloud
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources

interface CacheDataSource {

    fun addOrRemove(id: String, fact: FactCloud): FactUi

    fun fetch(factCacheCallback: FactCacheCallback)

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
