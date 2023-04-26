package com.example.chucknorrisfactsappmvvm.data.cache

import io.realm.Realm

interface ProvideRealm {
    fun provideRealm(): Realm
}