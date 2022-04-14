package com.example.e.addexpense.model

import com.example.e.BIG_DECIMAL_SCALE
import com.example.e.DEFAULT_ID_FOR_AUTOGENERATE
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.addexpense.participantpicker.ParticpantCardState.Companion.toParticipants
import com.example.e.changeSign
import com.example.e.domain.Expense
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

data class NewExpenseInput(
    val borrowers: List<ParticpantCardState>,
    val payers: List<ParticpantCardState>,
    val amount: BigDecimal,
    val title: String,
    val date: LocalDateTime,
) {
    fun toExpense() = Expense(
        id = DEFAULT_ID_FOR_AUTOGENERATE,
        title = title,
        participants = asParticipants(),
        date = date
    )

    private fun asParticipants() =
        borrowers.asParticipants(BigDecimal::changeSign) + payers.asParticipants()

    private fun List<ParticpantCardState>.asParticipants(additionalOperator: BigDecimal.() -> BigDecimal = { this }) =
        selected().toParticipants(amount.splitBetween(this).additionalOperator())

    private fun BigDecimal.splitBetween(participants: List<ParticpantCardState>): BigDecimal =
        divide(
            BigDecimal.valueOf(participants.selected().size.toDouble()),
            BIG_DECIMAL_SCALE,
            RoundingMode.HALF_UP
        )

    private fun List<ParticpantCardState>.selected() = filter { it.isSelected }
}
