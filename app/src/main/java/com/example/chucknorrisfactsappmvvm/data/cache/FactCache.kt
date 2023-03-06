package com.example.chucknorrisfactsappmvvm.data.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
* No OOP with Realm :(
**/

open class FactCache : RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var text: String = ""
    var punchline: String = ""
    var type: String = ""
}