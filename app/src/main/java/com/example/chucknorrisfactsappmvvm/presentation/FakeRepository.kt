package com.example.chucknorrisfactsappmvvm.presentation

import java.util.*

class FakeRepository(
    private val manageResources: ManageResources
) : Repository<Fact, Error> {

    private val noConnection = Error.NoConnection(manageResources)
    private val serviceUnavailable = Error.ServiceUnavailable(manageResources)
    private var callback: ResultCallBack<Fact, Error>? = null
    private var count = 0


    override fun fetch() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (count % 2 == 1) {
                    callback?.provideSuccess(Fact("fake count $count", "fact"))
                } else if (count % 3 == 0) {
                    callback?.provideError(noConnection)
                } else {
                    callback?.provideError(serviceUnavailable)
                }
                count++
            }
        }, 1000)
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<Fact, Error>) {
        this.callback = callback
    }
}