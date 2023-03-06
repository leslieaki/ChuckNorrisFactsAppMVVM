package com.example.chucknorrisfactsappmvvm.presentation

import androidx.annotation.DrawableRes
import com.example.chucknorrisfactsappmvvm.R

abstract class FactUi(
    private val text: String,
    private val punchline: String,
    @DrawableRes
    private val iconResId: Int
) {

    fun show(factUiCallback: FactUiCallback) = with(factUiCallback) {
        provideText("$text\n$punchline")
        provideIconResId(iconResId)
    }


    class Base(text: String, punchline: String) :
        FactUi(text, punchline, R.drawable.ic_favorite_unselected_24)

    class Favorite(text: String, punchline: String) :
        FactUi(text, punchline, R.drawable.ic_favorite_selected_24)

    class Failed(text: String) : FactUi(text, "", 0)
}
