package com.example.chucknorrisfactsappmvvm.data.cloud

import retrofit2.Call
import retrofit2.http.GET

interface FactService {

    @GET("random")
    fun fact(): Call<FactCloud>
}