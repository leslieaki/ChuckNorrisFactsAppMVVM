package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.presentation.FactUi
import com.example.chucknorrisfactsappmvvm.presentation.ManageResources

class FakeRepository(
    private val manageResources: ManageResources
) : Repository<FactUi, Error> {

    private val serviceError = Error.ServiceUnavailable(manageResources)

    private var callback: ResultCallBack<FactUi, Error>? = null

    private var count = 0

    override fun fetch() {
        when (++count % 3) {
            0 -> callback?.provideSuccess(FactUi.Base(""))
            1 -> callback?.provideSuccess(FactUi.Favorite(""))
            2 -> callback?.provideError(serviceError)
        }
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<FactUi, Error>) {
        this.callback = callback
    }
}