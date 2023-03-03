package com.example.chucknorrisfactsappmvvm.data

import com.example.chucknorrisfactsappmvvm.data.cloud.CloudDataSource
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloud
import com.example.chucknorrisfactsappmvvm.data.cloud.FactCloudCallback
import com.example.chucknorrisfactsappmvvm.presentation.FactUi

class BaseRepository(
    private val cloudDataSource: CloudDataSource
) : Repository<FactUi, Error> {

    private var callback: ResultCallBack<FactUi, Error>? = null

    override fun fetch() {
        cloudDataSource.fetch(object : FactCloudCallback {
            override fun provideFactCloud(factCloud: FactCloud) {
                callback?.provideSuccess(factCloud.toFact())
            }

            override fun provideError(error: Error) {
                callback?.provideError(error)
            }
        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallBack<FactUi, Error>) {
        this.callback = callback
    }
}