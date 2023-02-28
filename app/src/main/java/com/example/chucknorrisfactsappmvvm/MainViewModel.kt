package com.example.chucknorrisfactsappmvvm

class MainViewModel {

    private var textCallback: TextCallback = TextCallback.Empty()

    fun getFact() {
        //todo get data from server
        textCallback.provideText("result")
    }

    fun init(textCallback: TextCallback) {
        this.textCallback = textCallback
    }

    fun clear() {
        textCallback = TextCallback.Empty()
    }
}

interface TextCallback {

    fun provideText(text: String)

    class Empty : TextCallback {
        override fun provideText(text: String) = Unit
    }
}