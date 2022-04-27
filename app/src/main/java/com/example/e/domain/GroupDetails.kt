package com.example.e.domain

import com.example.e.ACCURACY
import com.example.e.DEFAULT_CURRENCY
import com.example.e.DEFAULT_ID_FOR_AUTOGENERATE
import com.example.e.domain.User.Companion.toParticipants
import com.example.e.main.group.GroupCardData
import com.example.e.main.spendings.HeaderCardData
import java.time.Clock
import java.time.LocalDateTime

data class GroupDetails(
    val accountingGroup: AccountingGroup,
    val expenses: List<Expense>,
    val members: List<User>
) : DomainModel {
    fun toHeaderCardData(groups: List<AccountingGroup>) = HeaderCardData(
        expenses.sumOf { it.totalAmount() }, DEFAULT_CURRENCY,
        GroupCardData(groups, accountingGroup.id)
    )

    fun getPayOffExpense(payOffText: String, clock: Clock): Expense? {
        return expense(payOffText, members.toParticipants(expenses), clock).let {
            if (it.totalAmount() <= ACCURACY) {
                null
            } else {
                it
            }
        }
    }

    private fun expense(
        payOffText: String,
        participantsInPayOf: List<Participant>,
        clock: Clock
    ) = Expense(
        id = DEFAULT_ID_FOR_AUTOGENERATE,
        title = payOffText + participantsInPayOf.map { it.user.name }
            .reduce { acc, s -> "$acc, $s" },
        participants = participantsInPayOf,
        date = LocalDateTime.now(clock)
    )
}
