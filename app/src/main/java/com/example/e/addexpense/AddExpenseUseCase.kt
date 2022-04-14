package com.example.e.addexpense

import androidx.lifecycle.MutableLiveData
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.data.Repository
import com.example.e.domain.GroupId
import com.example.e.domain.User.Companion.toParticipantCardState
import com.example.e.logThrowable
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private val repository: Repository) {
    suspend fun participants(
        groupId: GroupId,
        borrowersState: MutableLiveData<List<ParticpantCardState>>,
        payersState: MutableLiveData<List<ParticpantCardState>>,
    ) = repository.groupUsers(groupId).toParticipantCardState(SELECTED_BY_DEFAULT).also {
        borrowersState.value = it
        payersState.value = it
    }

    suspend fun addExpense(
        input: NewExpenseInput,
        groupId: GroupId,
        addExpenseEffect: MutableLiveData<AddExpenseEffect?>
    ) = try {
        addExpenseEffect.value = AddExpenseEffect.ShowProgressBar
        repository.addExpense(input.toExpense(), groupId)
        addExpenseEffect.value = AddExpenseEffect.GoToMainScreen
    } catch (e: Throwable) {
        e.logThrowable(this)
        addExpenseEffect.value = AddExpenseEffect.ShowGenericErrorMessage(e)
    }

    companion object {
        private const val SELECTED_BY_DEFAULT = false
    }
}
