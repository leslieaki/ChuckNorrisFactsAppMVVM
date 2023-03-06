package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactCache
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.google.gson.annotations.SerializedName


class FactCloud(
    @SerializedName("type")
    private val type: String,
    @SerializedName("setup")
    private val setup: String,
    @SerializedName("punchline")
    private val punchline: String,
    @SerializedName("id")
    private val id: Int,
) {
    fun toUi(): FactUi = FactUi.Base(setup, punchline)

    fun toFavoriteUi(): FactUi = FactUi.Favorite(setup, punchline)

    fun change(cacheDataSource: CacheDataSource): FactUi = cacheDataSource.addOrRemove(id, this)

    fun toCache(): FactCache {
        val factCache = FactCache()
        factCache.id = this.id
        factCache.text = this.setup
        factCache.punchline = this.punchline
        factCache.type = this.type
        return factCache
    }
}

