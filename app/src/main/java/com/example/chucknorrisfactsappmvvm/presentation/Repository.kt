package com.example.chucknorrisfactsappmvvm.presentation

interface Repository<S, E> {

    fun fetch()

    fun clear()

    fun init(callback: ResultCallBack<S, E>)

}

interface ResultCallBack<S, E> {

    fun provideSuccess(data: S)

    fun provideError(error: E)
}
