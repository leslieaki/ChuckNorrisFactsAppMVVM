package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.Fact
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
) : Fact {

    override fun <T> map(mapper: Fact.Mapper<T>): T = mapper.map(type, setup, punchline, id)
}

