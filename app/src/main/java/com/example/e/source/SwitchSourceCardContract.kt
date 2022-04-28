package com.example.e.source

interface SwitchSourceCardContract {
    fun switchSource(isSourceRemote: Boolean): Unit?
    fun onSourceSwitchedToTrue(): Unit?
    fun onSourceSwitchedToFalse(): Unit?
}
