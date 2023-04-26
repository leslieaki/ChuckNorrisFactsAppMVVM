package com.example.chucknorrisfactsappmvvm.data.cache

interface DataSource {
    suspend fun fetch(): FactResult
}