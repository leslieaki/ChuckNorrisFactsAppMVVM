package com.example.chucknorrisfactsappmvvm.data

class ToDomain : Fact.Mapper<FactDomain> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactDomain {
        return FactDomain(createdDate, iconUrl, id, updateDate, url, value)
    }
}