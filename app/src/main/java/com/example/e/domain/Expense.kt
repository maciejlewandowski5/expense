package com.example.e.domain

import com.example.e.changeSign
import com.example.e.data.ExpenseModel
import com.example.e.domain.Participant.Companion.getDebtors
import com.example.e.domain.Participant.Companion.getLoaners
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime

data class Expense(
    val id: Long,
    val title: String,
    val participants: List<Participant>,
    val date: LocalDateTime
) {
    fun totalAmount(): BigDecimal =
        participants.map { it.amount }.fold(BigDecimal.ZERO) { acc, amount ->
            acc.add(
                if (amount > BigDecimal.ZERO) {
                    amount
                } else {
                    BigDecimal.ZERO
                }
            )
        }

    fun toExpenseModel(groupId: GroupId): ExpenseModel =
        ExpenseModel(id = id, title = title, expanseDate = date, accountingGroupId = groupId.value)

    companion object {

        fun List<Expense>.of(user: User) = this
            .filter { expense -> expense.participants.firstOrNull { it.user.id == user.id } != null }
            .fold(emptyList<Participant>()) { a, e -> a + e.participants.filter { it.user.id == user.id } }

        fun Expense.splitExpense(clock: Clock): List<Expense> = createExpenses(
            participants.getDebtors().toMutableMap(),
            participants.getLoaners().toMutableMap(),
            clock
        ).filter { it.totalAmount() > BigDecimal.ZERO }
    }

    private fun createExpenses(
        debtors: MutableMap<Participant, BigDecimal>,
        loaners: MutableMap<Participant, BigDecimal>,
        clock: Clock
    ) = emptyList<Expense>().toMutableList().also {
        debtors.forEach { debtor ->
            loaners.forEach loanerLoop@{ loaner ->
                it.payDebt(debtor, loaner, debtors, loaners, clock)
                if (debtIsPayedOff(debtor)) {
                    return@loanerLoop
                }
            }
        }
    }

    private fun MutableList<Expense>.payDebt(
        debtor: Map.Entry<Participant, BigDecimal>,
        loaner: Map.Entry<Participant, BigDecimal>,
        debtors: MutableMap<Participant, BigDecimal>,
        loaners: MutableMap<Participant, BigDecimal>,
        clock: Clock
    ) {
        val payedAmountInTransaction = when {
            debtor.value > loaner.value -> {
                val payedAmountInTransaction = loaner.value
                debtors.replace(debtor.key, debtor.value.minus(loaner.value))
                loaners.replace(loaner.key, BigDecimal.ZERO)
                payedAmountInTransaction
            }
            debtor.value == loaner.value -> {
                val payedAmountInTransaction = debtor.value
                debtors.replace(debtor.key, BigDecimal.ZERO)
                loaners.replace(loaner.key, BigDecimal.ZERO)
                payedAmountInTransaction
            }
            else -> {
                val payedAmountInTransaction = debtor.value
                loaners.replace(loaner.key, loaner.value.minus(debtor.value))
                payedAmountInTransaction
            }
        }
        addExpense(debtor, loaner, payedAmountInTransaction, clock)
    }

    private fun MutableList<Expense>.addExpense(
        debtor: Map.Entry<Participant, BigDecimal>,
        loaner: Map.Entry<Participant, BigDecimal>,
        payedAmountInTransaction: BigDecimal,
        clock: Clock
    ) = add(
        Expense(
            id = 0,
            title = "${debtor.key.user.name} pays to ${loaner.key.user.name}",
            participants =
            listOf(
                Participant(
                    id = 0,
                    user = debtor.key.user,
                    amount = payedAmountInTransaction
                ),
                Participant(
                    id = 0,
                    user = loaner.key.user,
                    amount = payedAmountInTransaction.changeSign()
                )
            ),
            date = LocalDateTime.now(clock)
        )
    )

    private fun debtIsPayedOff(debtor: Map.Entry<Participant, BigDecimal>) =
        debtor.value > BigDecimal.ZERO
}
