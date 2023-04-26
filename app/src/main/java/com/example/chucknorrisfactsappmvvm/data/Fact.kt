package com.example.chucknorrisfactsappmvvm.data

interface Fact {
    suspend fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        suspend fun map(
            createdDate: String,
            iconUrl: String,
            id: String,
            updateDate: String,
            url: String,
            value: String
        ): T
    }
}