package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.cache.DataSource
import com.example.chucknorrisfactsappmvvm.data.cache.FactResult
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import java.net.UnknownHostException

interface CloudDataSource : DataSource {

    class Base(
        private val factService: FactService,
        private val manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy {
            Error.NoConnection(manageResources)
        }
        private val serviceError by lazy {
            Error.ServiceUnavailable(manageResources)
        }

        override suspend fun fetch(): FactResult = try {
            val response = factService.fact().execute()
            FactResult.Success(response.body()!!, false)
        } catch (e: Exception) {
            FactResult.Failure(
                if (e is UnknownHostException || e is java.net.ConnectException)
                    noConnection
                else
                    serviceError
            )
        }
    }
}