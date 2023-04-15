package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.jokesappmvvm.R

interface FactUi {

    fun show(factUiCallback: FactUiCallback)

    abstract class Abstract(
        private val value: String,

        @DrawableRes
        private val iconResId: Int
    ) : FactUi {

        override fun show(factUiCallback: FactUiCallback) = with(factUiCallback) {
            provideText(value)
            provideIconResId(iconResId)
        }
    }

    data class Base(private val value: String) :
        Abstract(value, R.drawable.ic_favorite_unselected_24)

    data class Favorite(private val value: String) :
        Abstract(value, R.drawable.ic_favorite_selected_24)

    data class Failed(private val value: String) : Abstract(value, 0)
}
