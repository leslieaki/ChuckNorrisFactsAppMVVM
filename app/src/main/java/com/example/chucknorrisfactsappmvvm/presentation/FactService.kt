package com.example.chucknorrisfactsappmvvm.presentation

import retrofit2.Call
import retrofit2.http.GET

interface FactService {

    @GET("random")
    fun fact(): Call<FactCloud>
}