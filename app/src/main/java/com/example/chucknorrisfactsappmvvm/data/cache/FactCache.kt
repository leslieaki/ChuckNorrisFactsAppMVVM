package com.example.chucknorrisfactsappmvvm.data.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
* No OOP with Realm :(
**/

open class FactCache : RealmObject() {
    @PrimaryKey
    var type: String = ""
    var setup: String = ""
    var punchline: String = ""
    var id: Int = -1
}