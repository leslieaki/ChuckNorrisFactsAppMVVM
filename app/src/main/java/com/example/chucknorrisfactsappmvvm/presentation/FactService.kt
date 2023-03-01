package com.example.chucknorrisfactsappmvvm.presentation

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface FactService {

    fun fact(callback: ServiceCallback)

    class Base() : FactService {
        override fun fact(callback: ServiceCallback) {
            Thread {
                var connection: HttpURLConnection? = null
                try {
                    val factUrl = URL(FACT_URL)
                    connection = factUrl.openConnection() as HttpURLConnection
                    InputStreamReader(BufferedInputStream(connection.inputStream)).use { BufferedReader::readText }

                } catch (e: Exception) {

                } finally {

                }

            }.start()
        }

        companion object {
            private const val FACT_URL = "https://api.chucknorris.io/jokes/random"
        }
    }
}

interface ServiceCallback {

    fun returnSuccess(data: String)

    fun returnError(errorType: ErrorType)
}

enum class ErrorType {
    NO_CONNECTION,
    OTHER
}