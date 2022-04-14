package com.example.e.addexpense

import com.example.e.R

sealed class AddExpenseEffect() {
    object GoToMainScreen : AddExpenseEffect()
    object ShowProgressBar : AddExpenseEffect()
    class ShowGenericErrorMessage<G : Throwable>(val value: G) : AddExpenseEffect()
    class ShowInputErrorMessage(val value: InputErrorTypes) : AddExpenseEffect()
}

enum class InputErrorTypes {
    TITLE, DATE, BORROWERS, AMOUNT, PAYERS, ALL;

    fun errorMessage(): Int = when (this) {
        InputErrorTypes.AMOUNT -> R.string.amountInputError
        else -> R.string.FixInputErrors
    }
}
