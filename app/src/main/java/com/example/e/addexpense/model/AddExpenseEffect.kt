package com.example.e.addexpense.model

import com.example.e.R
import java.math.BigDecimal
import java.time.LocalDateTime

sealed class AddExpenseEffect() {
    class GoToSplitScreen(val newExpenseInput: NewExpenseInput) : AddExpenseEffect() {
        constructor() : this(
            NewExpenseInput(
                emptyList(), emptyList(), BigDecimal.ZERO, "",
                LocalDateTime.now(),
                true
            )
        )
    }

    object ShowProgressBar : AddExpenseEffect()
    class ShowGenericErrorMessage<G : Throwable>(val value: G) : AddExpenseEffect()
    class ShowInputErrorMessage(val value: InputErrorTypes) : AddExpenseEffect()
}

enum class InputErrorTypes {
    TITLE, DATE, BORROWERS, AMOUNT, PAYERS, ALL;

    fun errorMessage(): Int = when (this) {
        AMOUNT -> R.string.amountInputError
        else -> R.string.FixInputErrors
    }
}
