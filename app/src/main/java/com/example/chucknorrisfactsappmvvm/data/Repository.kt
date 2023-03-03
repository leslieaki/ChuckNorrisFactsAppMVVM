package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.presentation.FactUi

interface Repository<S, E> {

    fun fetch()

    fun clear()

    fun init(callback: ResultCallBack<S, E>)

    abstract fun changeFactStatus(resultCallBack: ResultCallBack<FactUi, Error>)

    abstract fun chooseFavorites(favorites: Boolean)
}

interface ResultCallBack<S, E> {

    fun provideSuccess(data: S)

    fun provideError(error: E)
}
