package com.example.chucknorrisfactsappmvvm

class MainViewModel(private val repository: Repository<Fact, Error>) {

    private var textCallback: TextCallback = TextCallback.Empty()

    fun getFact() {
        repository.fetch()
    }

    fun init(textCallback: TextCallback) {
        this.textCallback = textCallback
        repository.init(object : ResultCallBack<Fact, Error> {
            override fun provideSuccess(data: Fact) {
                textCallback.provideText(data.getFactUi())
            }

            override fun provideError(error: Error) {
                textCallback.provideText(error.message())
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