package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.Fact

interface FactResult : Fact {

    fun toFavorite(): Boolean

    fun isSuccessful(): Boolean

    fun errorMessage(): String

    class Success(private val fact: Fact, private val toFavorite: Boolean) : FactResult {

        override fun toFavorite(): Boolean = toFavorite

        override fun isSuccessful(): Boolean = true

        override fun errorMessage(): String = ""

        override suspend fun <T> map(mapper: Fact.Mapper<T>): T {
            return fact.map(mapper)
        }
    }

    class Failure(private val error: Error) : FactResult {

        override fun toFavorite(): Boolean = false

        override fun isSuccessful(): Boolean = false

        override fun errorMessage(): String = error.message()

        override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
            throw java.lang.IllegalStateException()
    }
}