package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.Repository
import com.example.chucknorrisfactsappmvvm.data.ResultCallBack

class MainViewModel(private val repository: Repository<FactUi, Error>) {

    private var textCallback: TextCallback = TextCallback.Empty()
    private val resultCallBack = object : ResultCallBack<FactUi, Error> {

        override fun provideSuccess(data: FactUi) = data.show(textCallback)

        override fun provideError(error: Error) = FactUi.Failed(error.message()).show(textCallback)
    }

    fun init(textCallback: TextCallback) {
        this.textCallback = textCallback
        repository.init(resultCallBack)
    }

    fun getFact() {
        repository.fetch()
    }

    fun clear() {
        textCallback = TextCallback.Empty()
        repository.clear()
    }

    fun changeFavorite(isChecked: Boolean) {

    }
}

interface TextCallback {

    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)

    class Empty : TextCallback {

        override fun provideText(text: String) = Unit

        override fun provideIconResId(iconResId: Int) = Unit
    }
}