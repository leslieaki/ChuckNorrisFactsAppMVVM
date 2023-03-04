package com.example.chucknorrisfactsappmvvm.data.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
* No OOP with Realm :(
**/

open class FactCache : RealmObject() {
    @PrimaryKey
    var categories: List<Any> = emptyList()
    var createdAt: String = ""
    var iconUrl: String = ""
    var id: String = ""
    val updatedAt: String = ""
    var url: String = ""
    var value: String = ""

}