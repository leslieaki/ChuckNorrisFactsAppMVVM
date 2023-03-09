package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.FactResult
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface Repository<S, E> {

    suspend fun fetch(): FactResult

    suspend fun changeFactStatus(): FactUi

    fun chooseFavorites(favorites: Boolean)
}