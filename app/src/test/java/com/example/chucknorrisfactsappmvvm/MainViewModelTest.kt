package com.example.chucknorrisfactsappmvvm

import com.example.chucknorrisfactsappmvvm.presentation.*
import org.junit.Assert.assertEquals
import org.junit.Test


class MainViewModelTest {

    @Test
    fun test_success() {
        val repository = com.example.chucknorrisfactsappmvvm.presentation.FakeRepository()
        repository.returnSuccess = true
        val viewModel = MainViewModel(repository)
        viewModel.init(object : TextCallback {
            override fun provideText(text: String) {
                assertEquals("fake fact 1" + "\n" + "fact", text)
            }
        })
        viewModel.getFact()
        viewModel.clear()
    }

    @Test
    fun test_error() {
        val repository = com.example.chucknorrisfactsappmvvm.presentation.FakeRepository()
        repository.returnSuccess = false
        val viewModel = MainViewModel(repository)
        viewModel.init(object : TextCallback {
            override fun provideText(text: String) {
                assertEquals("fake error", text)
            }
        })
        viewModel.getFact()
        viewModel.clear()
    }
}

private class FakeRepository :
    Repository<Fact, com.example.chucknorrisfactsappmvvm.presentation.Error> {

    var returnSuccess = true
    private var callback: ResultCallBack<Fact, com.example.chucknorrisfactsappmvvm.presentation.Error>? = null

    override fun fetch() {
        if (returnSuccess)
            callback?.provideSuccess(Fact("fake fact 1", "fact"))
        else
            callback?.provideError(FakeError())
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<Fact, com.example.chucknorrisfactsappmvvm.presentation.Error>) {
        this.callback = callback
    }
}

private class FakeError : com.example.chucknorrisfactsappmvvm.presentation.Error {

    override fun message(): String {
        return "fake error"
    }
}
