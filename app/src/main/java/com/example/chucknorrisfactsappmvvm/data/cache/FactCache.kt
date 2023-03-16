package com.example.chucknorrisfactsappmvvm.data.cache

import com.example.chucknorrisfactsappmvvm.data.Fact
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
* No OOP with Realm :(
**/

open class FactCache : RealmObject(), Fact {
    @PrimaryKey
    var createdDate: String = ""
    var iconUrl: String = ""
    var id: String = ""
    var updateDate: String = ""
    var url: String = ""
    var value: String = ""

    override suspend fun <T> map(mapper: Fact.Mapper<T>): T =
        mapper.map(createdDate, iconUrl, id, updateDate, url, value)
}
