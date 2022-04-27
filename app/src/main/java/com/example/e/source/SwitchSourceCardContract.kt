package com.example.e.source

interface SwitchSourceCardContract {
    fun isSourceRemote(): Boolean
    fun switchSource(isSourceRemote: Boolean): Unit?
    fun onSourceSwitchedToTrue(): Unit?
    fun onSourceSwitchedToFalse(): Unit?
}
