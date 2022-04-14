package com.example.e.addexpense.view

import com.example.e.addexpense.participantpicker.ParticpantCardState

interface AddExpenseContentContract {
    fun setTitle(newTitle: String): Unit?
    fun borrowerClick(pickedParticipant: ParticpantCardState): Unit?
    fun setAmount(newAmount: String): Unit?
    fun payerClick(pickedPayer: ParticpantCardState): Unit?
}
