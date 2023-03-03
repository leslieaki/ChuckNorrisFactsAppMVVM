package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.chucknorrisfactsappmvvm.R

abstract class FactUi(
    private val fact: String,
    @DrawableRes private val iconResId: Int
) {

    fun show(factUiCallback: FactUiCallback) = with(factUiCallback) {
        provideText(fact)
        provideIconResId(iconResId)
    }


    class Base(fact: String) : FactUi(fact, R.drawable.ic_favorite_unselected_24)

    class Favorite(fact: String) : FactUi(fact, R.drawable.ic_favorite_selected_24)

    class Failed(fact: String) : FactUi(fact, 0)
}
