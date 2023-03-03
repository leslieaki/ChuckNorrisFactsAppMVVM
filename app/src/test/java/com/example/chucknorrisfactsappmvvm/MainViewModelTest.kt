package com.example.chucknorrisfactsappmvvm

import com.example.chucknorrisfactsappmvvm.data.*
import com.example.chucknorrisfactsappmvvm.data.cloud.FactService
import com.example.chucknorrisfactsappmvvm.presentation.*
import org.junit.Assert.assertEquals
import org.junit.Test


class MainViewModelTest {

    @Test
    fun test_success() {
        val repository = BaseRepository(
            FactService.Base(),
            ManageResources.Base()
        )
        repository.returnSuccess = true
        val viewModel = MainViewModel(repository)
        viewModel.init(object : FactUiCallback {
            override fun provideText(text: String) {
                assertEquals("fake fact 1" + "\n" + "fact", text)
            }
        })
        viewModel.getFact()
        viewModel.clear()
    }

    @Test
    fun test_error() {
        val repository = com.example.chucknorrisfactsappmvvm.data.FakeRepository()
        repository.returnSuccess = false
        val viewModel = MainViewModel(repository)
        viewModel.init(object : FactUiCallback {
            override fun provideText(text: String) {
                assertEquals("fake error", text)
            }
        })
        viewModel.getFact()
        viewModel.clear()
    }
}

private class FakeRepository :
    Repository<FactUi, Error> {

    var returnSuccess = true
    private var callback: ResultCallBack<FactUi, Error>? =
        null

    override fun fetch() {
        if (returnSuccess)
            callback?.provideSuccess(FactUi("fake fact 1", "fact"))
        else
            callback?.provideError(FakeError())
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<FactUi, Error>) {
        this.callback = callback
    }
}

private class FakeError : Error {

    override fun message(): String {
        return "fake error"
    }
}
