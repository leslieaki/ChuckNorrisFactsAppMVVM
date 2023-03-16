package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.Fact
import com.google.gson.annotations.SerializedName


class FactCloud(
    @SerializedName("categories")
    private val categories: List<Any>,
    @SerializedName("created_at")
    private val createdDate: String,
    @SerializedName("icon_url")
    private val iconUrl: String,
    @SerializedName("id")
    private val id: String,
    @SerializedName("updated_at")
    private val updateDate: String,
    @SerializedName("url")
    private val url: String,
    @SerializedName("value")
    private val value: String,
) : Fact {

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
        mapper.map(createdDate, iconUrl, id, updateDate, url, value)
}