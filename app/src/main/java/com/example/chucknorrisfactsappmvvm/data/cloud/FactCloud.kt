package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.cache.CacheDataSource
import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.google.gson.annotations.SerializedName


data class FactCloud(
    @SerializedName("categories")
    private val categories: String,
    @SerializedName("created_at")
    private val created_at: String,
    @SerializedName("icon_url")
    private val icon_url: String,
    @SerializedName("id")
    private val id: String,
    @SerializedName("updated_at")
    private val updated_at: String,
    @SerializedName("url")
    private val url: String,
    @SerializedName("value")
    private val value: String
) {
    fun toUi(): FactUi = FactUi.Base(value)

    fun toFavoriteUi(): FactUi = FactUi.Favorite(value)

    fun change(cacheDataSource: CacheDataSource): FactUi = cacheDataSource.addOrRemove(id, this)
}