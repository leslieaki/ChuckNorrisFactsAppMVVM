package com.example.chucknorrisfactsappmvvm.presentation

interface FactLiveDataWrapper: LiveDataWrapper<FactUi> {

    class Base :LiveDataWrapper.Abstract<FactUi>(), FactLiveDataWrapper
}