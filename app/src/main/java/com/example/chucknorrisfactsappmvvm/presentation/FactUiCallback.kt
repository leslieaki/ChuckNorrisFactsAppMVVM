package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes

interface FactUiCallback {

    fun provideText(text: String)

    fun provideIconResId(@DrawableRes iconResId: Int)
}