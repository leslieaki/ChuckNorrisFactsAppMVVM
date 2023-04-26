package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class ToFavoriteUi : Fact.Mapper<FactUi> {
    override suspend fun map(
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactUi {
        return FactUi.Favorite(value)
    }
}