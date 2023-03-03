package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloud
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface CacheDataSource {

    fun addOrRemove(id: String, Fact: FactCloud): FactUi
}