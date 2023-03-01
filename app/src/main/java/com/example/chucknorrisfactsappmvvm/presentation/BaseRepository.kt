package com.example.chucknorrisfactsappmvvm.presentation

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class BaseRepository(
    private val factService: FactService,
    private val manageResources: ManageResources
) : Repository<Fact, Error> {

    private val noConnection = Error.NoConnection(manageResources)
    private val serviceError = Error.ServiceUnavailable(manageResources)
    private var callback: ResultCallBack<Fact, Error>? = null

    override fun fetch() {
        factService.fact().enqueue(object : Callback<FactCloud> {
            override fun onResponse(call: Call<FactCloud>, response: Response<FactCloud>) {
                if (response.isSuccessful) {
                    callback?.provideSuccess(response.body()!!.toFact())
                } else {
                    callback?.provideError(serviceError)
                }
            }

            override fun onFailure(call: Call<FactCloud>, t: Throwable) {
                if (t is UnknownHostException || t is java.net.ConnectException) {
                    callback?.provideError(noConnection)
                } else {
                    callback?.provideError(serviceError)
                }
            }
        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<Fact, Error>) {
        this.callback = callback
    }
}