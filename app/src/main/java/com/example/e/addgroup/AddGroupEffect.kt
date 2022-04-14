package com.example.e.addgroup

sealed class AddGroupEffect() {
    object ShowLoadingIndicator : AddGroupEffect()
    object GoToMainScreen : AddGroupEffect()
    object ShowErrorMessage : AddGroupEffect()
}
