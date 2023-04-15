package com.example.chucknorrisfactsappmvvm.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface Observe<T : Any> {

    fun observe(owner: LifecycleOwner, observer: Observer<T>) = Unit
}