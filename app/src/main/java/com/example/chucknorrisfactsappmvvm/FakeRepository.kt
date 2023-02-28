package com.example.chucknorrisfactsappmvvm

import java.util.*

class FakeRepository : Repository<Any, Any> {

    private var callback: ResultCallBack<Any, Any>? = null
    private var count = 0


    override fun fetch() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (++count % 2 == 1) {
                    callback?.provideSuccess("successData")
                } else {
                    callback?.provideError("errorData")
                }
            }
        }, 1000)
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<Any, Any>) {
        this.callback = callback
    }
}