package com.example.e.login.screen

sealed class LoginEffect {
    object GoToMainScreen : LoginEffect()
    object LoginInProgress : LoginEffect()
    class ShowErrorMessage(val error: Throwable) : LoginEffect()
}
