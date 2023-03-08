package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cache.FactResult
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface Repository<S, E> {

    fun fetch(): FactResult

    fun changeFactStatus(): FactUi

    fun chooseFavorites(favorites: Boolean)
}