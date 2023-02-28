package com.example.chucknorrisfactsappmvvm

class MainViewModel(private val repository: Repository<Any, Any>) {

    private var textCallback: TextCallback = TextCallback.Empty()

    fun getFact() {
        repository.fetch()
    }

    fun init(textCallback: TextCallback) {
        this.textCallback = textCallback
        repository.init(object : ResultCallBack<Any, Any> {
            override fun provideSuccess(data: Any) {
                textCallback.provideText("success")
            }

            override fun provideError(error: Any) {
                textCallback.provideText("error")
            }

        })
    }

    fun clear() {
        textCallback = TextCallback.Empty()
        repository.clear()
    }
}

interface TextCallback {

    fun provideText(text: String)

    class Empty : TextCallback {
        override fun provideText(text: String) = Unit
    }
}