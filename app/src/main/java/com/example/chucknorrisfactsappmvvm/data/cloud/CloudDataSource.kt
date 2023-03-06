package com.example.chucknorrisfactsappmvvm.data.cloud

import com.example.chucknorrisfactsappmvvm.data.cache.FactCallback
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

interface CloudDataSource {

    fun fetch(cloudCallback: FactCallback)

    class Base(
        private val factService: FactService,
        private val manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy {
            com.example.chucknorrisfactsappmvvm.data.Error.NoConnection(manageResources)
        }
        private val serviceError by lazy {
            com.example.chucknorrisfactsappmvvm.data.Error.ServiceUnavailable(manageResources)
        }

        override fun fetch(cloudCallback: FactCallback) {
            factService.fact().enqueue(object : Callback<FactCloud> {

                override fun onResponse(call: Call<FactCloud>, response: Response<FactCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null)
                            cloudCallback.provideError(serviceError)
                        else
                            cloudCallback.provideFact(body)
                    } else
                        cloudCallback.provideError(serviceError)
                }

                override fun onFailure(call: Call<FactCloud>, t: Throwable) {
                    cloudCallback.provideError(
                        if (t is UnknownHostException || t is java.net.ConnectException)
                            noConnection
                        else
                            serviceError
                    )
                }
            })
        }
    }
}