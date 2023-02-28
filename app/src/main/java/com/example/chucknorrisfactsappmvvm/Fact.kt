package com.example.chucknorrisfactsappmvvm

class Fact(private val text: String, private val fact: String) {

    fun getFactUi() = "$text\n$fact"
}