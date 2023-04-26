package com.example.chucknorrisfactsappmvvm.data

data class FactDomain(
    private val createdDate: String,
    private val iconUrl: String,
    private val id: String,
    private val updateDate: String,
    private val url: String,
    private val value: String
) : Fact {

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
        mapper.map(createdDate, iconUrl, id, updateDate, url, value)
}