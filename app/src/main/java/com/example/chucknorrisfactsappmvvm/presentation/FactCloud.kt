package com.example.chucknorrisfactsappmvvm.presentation

import com.google.gson.annotations.SerializedName


data class FactCloud(
    @SerializedName("categories")
    private val categories: List<Any>,
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
    fun toFact(): Fact {
        return Fact(value)
    }
}