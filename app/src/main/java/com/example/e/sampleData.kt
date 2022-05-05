package com.example.e

import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.domain.*
import com.example.e.domain.User.Companion.toParticipants
import com.example.e.main.group.GroupCardData
import com.example.e.main.spendings.HeaderCardData
import java.math.BigDecimal
import java.time.LocalDateTime

val sampleUsers = listOf(
    User("alan", "alan kowalskidsdofksdokfidksfikdifkdiskfidfkfidkfidfkdiskf"),
    User("testlwy", "alan testowy"),
    User("2", "asdsadlski"),
    User("3", "alan kod sd asd walski"),
)

val sampleGroups = listOf(
    AccountingGroup(GroupId(1), "group1"),
    AccountingGroup(GroupId(2), "gro up1"),
    AccountingGroup(GroupId(3), "group1 asd"),
    AccountingGroup(GroupId(4), "grs"),
    AccountingGroup(GroupId(5), " "),
    AccountingGroup(GroupId(6), "group1"),
    AccountingGroup(GroupId(7), "group1"),
    AccountingGroup(GroupId(8), "group1"),
    AccountingGroup(GroupId(9), "group1"),
)

val sampleDateTime = LocalDateTime.of(12, 12, 12, 12, 12)

val sampleExpense = Expense(
    123L,
    "ko",
    participants = listOf(
        Participant(1, sampleUsers[0], BigDecimal("1000000000006.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50"))
    ),
    date = sampleDateTime
)

fun sampleExpense(id: Long) = Expense(
    id,
    "ko",
    participants = listOf(
        Participant(1, sampleUsers[0], BigDecimal("1000000000006.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50")),
        Participant(1, sampleUsers[0], BigDecimal("16.50"))
    ),
    date = sampleDateTime
)

val sampleExpenses = listOf(
    sampleExpense(0),
    sampleExpense(1),
    sampleExpense(2),
    sampleExpense(3),
    sampleExpense(4),
    sampleExpense(5),
    sampleExpense(6),
    sampleExpense(7),
    sampleExpense(8),
    sampleExpense(9),
    sampleExpense(10),
    sampleExpense(11),
    sampleExpense(12),
    sampleExpense(13),
    sampleExpense(14),

)

val sampleConversation =
    HeaderCardData(BigDecimal("16.50"), "PLN", GroupCardData(sampleGroups, GroupId(2)))

val sampleGroupDetails = GroupDetails(
    accountingGroup = AccountingGroup(GroupId(1), "Asda"), sampleExpenses,
    sampleUsers
)
val sampleParticipantCardState = listOf(
    ParticpantCardState(
        User("1", "Janusz kowalski"),
        true
    ),
    ParticpantCardState(
        User("1", "Weronika Testowa"),
        false,
    ),
    ParticpantCardState(
        User("1", "Konrad Testerski"),
        false,
    ),
    ParticpantCardState(
        User("1", "Konrad Testerski"),
        false,
    ),
    ParticpantCardState(
        User("1", "Konrad Testerski"),
        false,
    ),
    ParticpantCardState(
        User("1", "Konrad Testerski"),
        false,
    ),
    ParticpantCardState(
        User("1", "Konrad Testerski"),
        false
    ),

)
val sampleParticipant = sampleUsers.toParticipants(sampleExpenses)
